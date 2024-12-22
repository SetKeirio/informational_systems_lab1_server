package informational_systems.lab1.controllers;

import informational_systems.lab1.items.*;
import informational_systems.lab1.services.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spacemarines")
public class SpaceMarineController {
    @Autowired
    private SpaceMarineService spaceMarineService;

    @Autowired
    private AstartesCategoryService astartesCategoryService;

    @Autowired
    private MeleeWeaponService meleeWeaponService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private CoordinatesService coordinatesService;

    @Autowired
    private UserSpaceMarinesService userSpaceMarinesService;

    @Autowired
    private AuthentifyService authService;

    @Autowired
    private ObjectHistoryService objectHistoryService;

    @GetMapping
    public List<SpaceMarine> getAllSpaceMarines() {
        return spaceMarineService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<String> deleteSpaceMarine(@PathVariable int id, HttpServletRequest request) {
        // Получаем токен из заголовков запроса
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"
        if (!authService.isTokenValid(token)){
            return ResponseEntity.status(403).body(null);
        }
        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class); // Извлекаем роль из токена
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена (или передать по-другому)

        // Проверяем, существует ли космодесантник
        SpaceMarine marine = spaceMarineService.findById(id);
        if (marine == null) {
            return ResponseEntity.notFound().build();
        }

        // Если пользователь admin, он может удалить любой объект
        if ("ADMIN".equals(role)) {
            ObjectHistory objectHistory = new ObjectHistory();
            objectHistory.setSpaceMarineId(id);
            objectHistory.setUserId(userIdFromToken);
            objectHistory.setAction("DELETE");
            objectHistory.setTimestamp(LocalDateTime.now());
            // Сохраняем запись в истории
            objectHistoryService.save(objectHistory);
            spaceMarineService.delete(id);
            return ResponseEntity.ok("Космодесантник удален.");
        }

        // Если пользователь user, проверяем, что космодесантник принадлежит ему
        if ("USER".equals(role)) {
            // Проверяем, является ли этот космодесантник принадлежащим текущему пользователю
            boolean belongsToUser = userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, id);
            if (!belongsToUser) {
                return ResponseEntity.status(405).body("Вы не можете удалить чужой объект.");
            }
            ObjectHistory objectHistory = new ObjectHistory();
            objectHistory.setSpaceMarineId(id);
            objectHistory.setUserId(userIdFromToken);
            objectHistory.setAction("DELETE");
            objectHistory.setTimestamp(LocalDateTime.now());
            // Сохраняем запись в истории
            objectHistoryService.save(objectHistory);
            spaceMarineService.delete(id);
            return ResponseEntity.ok("Космодесантник удален.");
        }

        return ResponseEntity.status(403).body("Доступ запрещен.");
    }

    @PostMapping("/create")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<SpaceMarine> createSpaceMarine(@RequestBody SpaceMarineRequest request, HttpServletRequest r) {
        // Convert SpaceMarineRequest to SpaceMarine entity
        String token = r.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"
        if (!authService.isTokenValid(token)){
            return ResponseEntity.status(403).body(null);
        }
        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена (или передать по-другому)
        SpaceMarine marine = new SpaceMarine();

        boolean nameExists = spaceMarineService.existsByName(request.getName());
        if (nameExists) {
            return ResponseEntity.status(419).body(null); // Если имя уже существует, возвращаем ошибку с кодом 400
        }

        marine.setName(request.getName());

        // Assuming you have a method to convert coordinates
        Coordinates coordinates = new Coordinates();
        coordinates.setX(request.getCoordinatesId().getX());
        coordinates.setY(request.getCoordinatesId().getY());
        coordinatesService.save(coordinates);
        marine.setCoordinatesId(coordinates.getId());

        marine.setChapterId(request.getChapterId());
        marine.setHealth(request.getHealth());

        // Convert category, weaponType, and meleeWeapon to their respective IDs if needed
        Integer categoryId = null;
        try {
            categoryId = astartesCategoryService.findIdByName(request.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategory()));
        }
        catch (RuntimeException e){
            categoryId = null;
        }
        int weaponTypeId = weaponService.findIdByName(request.getWeaponType())
                .orElseThrow(() -> new RuntimeException("Weapon type not found: " + request.getWeaponType()));
        int meleeWeaponId = meleeWeaponService.findIdByName(request.getMeleeWeapon())
                .orElseThrow(() -> new RuntimeException("Melee weapon not found: " + request.getMeleeWeapon()));

        marine.setCategoryId(categoryId);
        marine.setWeaponTypeId(weaponTypeId);
        marine.setMeleeWeaponId(meleeWeaponId);
        marine.setCreationDate(LocalDateTime.now());
        SpaceMarine savedMarine = spaceMarineService.save(marine);
        ObjectHistory objectHistory = new ObjectHistory();
        objectHistory.setSpaceMarineId(savedMarine.getId());
        objectHistory.setUserId(userIdFromToken);
        objectHistory.setAction("CREATE");
        objectHistory.setTimestamp(LocalDateTime.now());
        // Сохраняем запись в истории
        objectHistoryService.save(objectHistory);
        UserSpaceMarines userSpaceMarines = new UserSpaceMarines();
        userSpaceMarines.setUserId(userIdFromToken);
        userSpaceMarines.setSpaceMarineId(savedMarine.getId());
        UserSpaceMarines savedUserMarine = userSpaceMarinesService.save(userSpaceMarines);
        return ResponseEntity.ok(savedMarine);
    }

    @GetMapping("/average-health")
    public ResponseEntity<Double> getAverageHealth() {
        List<SpaceMarine> spaceMarines = spaceMarineService.findAll();
        Double averageHealth = spaceMarines.stream()
                .filter(marine -> marine.getHealth() != null) // Исключаем null значения
                .mapToInt(SpaceMarine::getHealth)
                .average()
                .orElse(0.0); // Возвращаем 0.0, если нет ни одного значения

        return ResponseEntity.ok(averageHealth);
    }

    @GetMapping("/category-greater-than/{categoryId}")
    public ResponseEntity<Integer> getCountOfSpaceMarinesByCategoryIdGreaterThan(@PathVariable int categoryId) {
        long count = spaceMarineService.findAll().stream()
                .filter(marine -> marine.getCategoryId() != null && marine.getCategoryId() > categoryId)
                .count();
        return ResponseEntity.ok((int) count);
    }

    @GetMapping("/belongsToUser/{id}")
    public ResponseEntity<Boolean> getCountOfSpaceMarinesByCategoryIdGreaterThan(@PathVariable int id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"

        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class); // Извлекаем роль из токена
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена (или передать по-другому)

        // Проверяем, существует ли космодесантник
        SpaceMarine marine = spaceMarineService.findById(id);
        if (marine == null) {
            return ResponseEntity.notFound().build();
        }
        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(Boolean.TRUE);
        }

        if ("USER".equals(role)) {
            boolean belongsToUser = userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, id);
            if (!belongsToUser) {
                return ResponseEntity.status(403).body(null); // Доступ запрещен
            }
            return ResponseEntity.ok(Boolean.TRUE);
        }

        return ResponseEntity.status(403).body(null); // Возвращаем 404, если космодесантник не найден
    }

    @PutMapping("/update/{id}")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<SpaceMarine> updateSpaceMarine(@PathVariable int id, @RequestBody SpaceMarineRequest request, HttpServletRequest r) {
        // Получаем токен из заголовков запроса
        String token = r.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"

        // Проверяем валидность токена
        if (!authService.isTokenValid(token)) {
            return ResponseEntity.status(403).body(null);
        }

        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена

        // Ищем космодесантника по id
        SpaceMarine marine = spaceMarineService.findById(id);
        if (marine == null) {
            return ResponseEntity.notFound().build();  // Космодесантник не найден
        }

        // Проверяем, принадлежит ли космодесантник текущему пользователю
        if (!userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, id) && !claims.get("role", String.class).equals("ADMIN")) {
            return ResponseEntity.status(403).body(null);  // Доступ запрещен, если это не админ или не его космодесантник
        }

        // Обновляем данные космодесантника на основе полученного запроса
        marine.setName(request.getName());

        // Обновляем координаты
        Coordinates coordinates = coordinatesService.findById(marine.getCoordinatesId());
        if (coordinates != null) {
            coordinates.setX(request.getCoordinatesId().getX());
            coordinates.setY(request.getCoordinatesId().getY());
            coordinatesService.save(coordinates);
        } else {
            // Если координаты не найдены, создаем новые
            coordinates = new Coordinates();
            coordinates.setX(request.getCoordinatesId().getX());
            coordinates.setY(request.getCoordinatesId().getY());
            coordinatesService.save(coordinates);
            marine.setCoordinatesId(coordinates.getId());
        }

        // Обновляем остальные параметры
        marine.setChapterId(request.getChapterId());
        marine.setHealth(request.getHealth());

        // Преобразуем категорию, оружие и ближнее оружие
        Integer categoryId = null;
        try {
            categoryId = astartesCategoryService.findIdByName(request.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategory()));
        }
        catch (RuntimeException e){
        }
        int weaponTypeId = weaponService.findIdByName(request.getWeaponType())
                .orElseThrow(() -> new RuntimeException("Weapon type not found: " + request.getWeaponType()));
        int meleeWeaponId = meleeWeaponService.findIdByName(request.getMeleeWeapon())
                .orElseThrow(() -> new RuntimeException("Melee weapon not found: " + request.getMeleeWeapon()));

        marine.setCategoryId(categoryId);
        marine.setWeaponTypeId(weaponTypeId);
        marine.setMeleeWeaponId(meleeWeaponId);

        // Сохраняем обновленный космодесантник
        SpaceMarine savedMarine = spaceMarineService.save(marine);

        // Логируем изменения в истории
        ObjectHistory objectHistory = new ObjectHistory();
        objectHistory.setSpaceMarineId(savedMarine.getId());
        objectHistory.setUserId(userIdFromToken);
        objectHistory.setAction("UPDATE");
        objectHistory.setTimestamp(LocalDateTime.now());
        objectHistoryService.save(objectHistory);

        // Возвращаем обновленный космодесантник
        return ResponseEntity.ok(savedMarine);
    }

    @GetMapping("/exclude/{id}")
    public ResponseEntity<List<SpaceMarineResponse>> excludeSpaceMarineFromCategory(@PathVariable int id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"

        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class); // Извлекаем роль из токена
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена (или передать по-другому)

        // Проверяем, существует ли космодесантник
        SpaceMarine marine = spaceMarineService.findById(id);
        if (marine == null) {
            return ResponseEntity.notFound().build();
        }
        if ("ADMIN".equals(role)) {
            marine.setCategoryId(null); // Устанавливаем категорию в null
            spaceMarineService.save(marine); // Сохраняем изменения
            SpaceMarineResponse marineResponse = spaceMarineService.getSpaceMarineById(marine.getId());
            List<SpaceMarineResponse> spaceMarines = new ArrayList<>();
            spaceMarines.add(marineResponse);
            return ResponseEntity.ok(spaceMarines);
        }

        if ("USER".equals(role)) {
            boolean belongsToUser = userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, id);
            if (!belongsToUser) {
                return ResponseEntity.status(403).body(null); // Доступ запрещен
            }
            marine.setCategoryId(null); // Устанавливаем категорию в null
            spaceMarineService.save(marine); // Сохраняем изменения
            SpaceMarineResponse marineResponse = spaceMarineService.getSpaceMarineById(marine.getId());
            List<SpaceMarineResponse> spaceMarines = new ArrayList<>();
            spaceMarines.add(marineResponse);
            return ResponseEntity.ok(spaceMarines);
        }

        return ResponseEntity.status(403).body(null); // Возвращаем 404, если космодесантник не найден
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<SpaceMarine>> getSpaceMarinesByCategoryForUser (@PathVariable String categoryName, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"
        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class); // Извлекаем роль из токена
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена
        // Получаем ID категории по имени
        Integer categoryId = astartesCategoryService.findIdByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        // Получаем все космодесантники, у которых задана эта категория
        List<SpaceMarine> spaceMarinesWithCategory = spaceMarineService.findByCategoryId(categoryId);
        List<SpaceMarine> spaceMarinesToReturn;
        // Если пользователь администратор, возвращаем всех космодесантников с данной категорией
        if ("ADMIN".equals(role)) {
            spaceMarinesToReturn = spaceMarinesWithCategory;
        }
        else if ("USER".equals(role)) {
            spaceMarinesToReturn = spaceMarinesWithCategory.stream()
                    .filter(marine -> userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, marine.getId()))
                    .collect(Collectors.toList());
        } else {
            return ResponseEntity.status(403).body(null); // Доступ запрещен для других ролей
        }
        // Фильтруем их по тому, принадлежат ли они текущему пользователю
        return ResponseEntity.ok(spaceMarinesToReturn);
    }

    @GetMapping("/remove-category/{categoryId}")
    public ResponseEntity<String> removeCategoryFromSpaceMarines(@PathVariable Integer categoryId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"

        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class);
        int userIdFromToken = claims.get("userId", Integer.class);
        if ("ADMIN".equals(role)) {
            int updatedCount = spaceMarineService.removeAstartesCategoryFromSpaceMarines(categoryId);
            return ResponseEntity.ok("Категория с ID " + categoryId + " была успешно удалена у " + updatedCount + " космодесантников.");
        }
        if ("USER".equals(role)) {
            // Получаем все космодесантники, у которых задана эта категория
            List<SpaceMarine> spaceMarinesWithCategory = spaceMarineService.findByCategoryId(categoryId);

            // Фильтруем их по тому, принадлежат ли они текущему пользователю
            List<SpaceMarine> spaceMarinesToUpdate = spaceMarinesWithCategory.stream()
                    .filter(marine -> userSpaceMarinesService.doesUserOwnMarine(userIdFromToken, marine.getId()))
                    .collect(Collectors.toList());

            // Удаляем категорию только у этих космодесантников
            int updatedCount = 0;
            for (SpaceMarine marine : spaceMarinesToUpdate) {
                marine.setCategoryId(null); // Устанавливаем категорию в null
                spaceMarineService.save(marine); // Сохраняем изменения
                updatedCount++;
            }

            return ResponseEntity.ok("Категория с ID " + categoryId + " была успешно удалена у " + updatedCount + " космодесантников.");
        }
        return ResponseEntity.status(403).body("Доступ запрещен.");
    }

    @GetMapping("/table")
    public ResponseEntity<List<SpaceMarineResponse>> getAllSpaceMarinesDetailed() {
        List<SpaceMarineResponse> spaceMarines = spaceMarineService.getAllSpaceMarines();
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("/health-less-than/{maxHealth}")
    public ResponseEntity<List<SpaceMarineResponse>> getSpaceMarinesByHealthLessThan(@PathVariable int maxHealth) {
        List<SpaceMarineResponse> spaceMarines = spaceMarineService.getSpaceMarinesWithHealthLessThan(maxHealth);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMarine> getSpaceMarineById(@PathVariable int id) {
        SpaceMarine spaceMarine = spaceMarineService.findById(id);
        if (spaceMarine != null) {
            return ResponseEntity.ok(spaceMarine);
        }
        return ResponseEntity.notFound().build(); // Возвращаем 404, если космодесантник не найден
    }

}
