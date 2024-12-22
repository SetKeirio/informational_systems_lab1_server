package informational_systems.lab1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import informational_systems.lab1.items.*;
import informational_systems.lab1.services.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/importhistory")
public class ImportHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ImportHistoryController.class);

    @Autowired
    private ImportHistoryService importHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthentifyService authService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ObjectMapper objectMapper;

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
    private ObjectHistoryService objectHistoryService;

    // Эндпоинт для создания записи импорта
    @PostMapping("/add")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ImportHistory> createImportHistory(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ImportHistory answer = new ImportHistory();
        // Логируем получение файла
        logger.info("Received file with name: {}", file.getOriginalFilename());
        // Проверяем валидность токена и извлекаем роль
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"
        if (!authService.isTokenValid(token)){
            return ResponseEntity.status(403).body(answer);
        }
        // Проверяем валидность токена и извлекаем роль
        Claims claims = authService.getClaimsFromToken(token);
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена (или передать по-другому)

        // Проверка на пустой файл
        if (file.isEmpty()) {
            logger.error("File is empty");
            answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "error", 0, "Empty file");
            return ResponseEntity.status(400).body(null);
        }

        // Чтение содержимого файла
        List<SpaceMarineDTO> importHistoryDTOList = new ArrayList<SpaceMarineDTO>();
        List<SpaceMarine> spaceMarineList = new ArrayList<SpaceMarine>();
        List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
        List<Chapter> chapterList = new ArrayList<Chapter>();
        List<ObjectHistory> objectHistoryList = new ArrayList<ObjectHistory>();
        List<UserSpaceMarines> userSpaceMarinesList = new ArrayList<UserSpaceMarines>();
        List<SpaceMarine> spaceMarinesList = spaceMarineService.findAll();
        List<String> spaceMarineNames = new ArrayList<String>();
        for (SpaceMarine spaceMarine: spaceMarinesList){
            spaceMarineNames.add(spaceMarine.getName());
        }

        try {
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            fileContent = fileContent.trim();
            // Проверка на наличие скобок в начале и в конце JSON
            if (!fileContent.startsWith("[")) {
                fileContent = "[" + fileContent; // Добавляем "[" в начало
            }
            if (!fileContent.endsWith("]")) {
                fileContent = fileContent + "]"; // Добавляем "]" в конец
            }

            // Преобразуем содержимое файла JSON в список объектов SpaceMarineDTO
            importHistoryDTOList = objectMapper.readValue(fileContent, objectMapper.getTypeFactory().constructCollectionType(List.class, SpaceMarineDTO.class));

            // Логируем полученные данные
            logger.info("Received import history data:");
            for (SpaceMarineDTO importHistoryDTO : importHistoryDTOList) {
                logger.info("Name = {}, Coordinates = ({}, {}), Chapter = {}, Health = {}, Category = {}, Weapon = {}, MeleeWeapon = {}",
                        importHistoryDTO.getName(),
                        importHistoryDTO.getCoordinates().getX(),
                        importHistoryDTO.getCoordinates().getY(),
                        importHistoryDTO.getChapter().getName(),
                        importHistoryDTO.getHealth(),
                        importHistoryDTO.getCategory(),
                        importHistoryDTO.getWeapon(),
                        importHistoryDTO.getMeleeWeapon());
            }
            for (SpaceMarineDTO importHistory: importHistoryDTOList){

                SpaceMarine marine = new SpaceMarine();
                marine.setName(importHistory.getName());

                for (String name: spaceMarineNames){
                    if (importHistory.getName().equals(name)){
                        logger.error("This name exists");
                        answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "error", 0, "This name exists");
                        return ResponseEntity.status(409).body(answer);
                    }
                }

                spaceMarineNames.add(importHistory.getName());

                // Assuming you have a method to convert coordinates
                Coordinates coordinates = new Coordinates();
                coordinates.setX(importHistory.getCoordinates().getX());
                coordinates.setY(importHistory.getCoordinates().getY());
                coordinatesList.add(coordinates);
                marine.setCoordinatesId(coordinates.getId());
                Chapter chapter = new Chapter();
                chapter.setName(importHistory.getChapter().getName());
                chapter.setMarinesCount(importHistory.getChapter().getMarinesCount());
                chapterList.add(chapter);
                marine.setHealth(importHistory.getHealth());

                // Convert category, weaponType, and meleeWeapon to their respective IDs if needed
                Integer categoryId = null;
                try {
                    categoryId = astartesCategoryService.findIdByName(importHistory.getCategory())
                            .orElseThrow(() -> new RuntimeException("Category not found: " + importHistory.getCategory()));
                }
                catch (RuntimeException e){
                    categoryId = null;
                }
                int weaponTypeId;
                int meleeWeaponId;
                try {
                    weaponTypeId = weaponService.findIdByName(importHistory.getWeapon())
                            .orElseThrow(() -> new RuntimeException("Weapon type not found: " + importHistory.getWeapon()));
                    meleeWeaponId = meleeWeaponService.findIdByName(importHistory.getMeleeWeapon())
                            .orElseThrow(() -> new RuntimeException("Melee weapon not found: " + importHistory.getMeleeWeapon()));
                }
                catch (RuntimeException e){
                    logger.error("Weapon and MeleeWeapon can not be null", e);
                    answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "error", 0, "Weapon and MeleeWeapon are not null and enum");
                    return ResponseEntity.status(400).body(answer);
                }
                marine.setCategoryId(categoryId);
                marine.setWeaponTypeId(weaponTypeId);
                marine.setMeleeWeaponId(meleeWeaponId);
                marine.setCreationDate(LocalDateTime.now());
                spaceMarineList.add(marine);
                // SpaceMarine savedMarine = spaceMarineService.save(marine);
                ObjectHistory objectHistory = new ObjectHistory();
                // objectHistory.setSpaceMarineId(savedMarine.getId());
                objectHistory.setUserId(userIdFromToken);
                objectHistory.setAction("CREATE");
                objectHistory.setTimestamp(LocalDateTime.now());
                objectHistoryList.add(objectHistory);
                UserSpaceMarines userSpaceMarines = new UserSpaceMarines();
                userSpaceMarines.setUserId(userIdFromToken);
                // userSpaceMarines.setSpaceMarineId(savedMarine.getId());
                userSpaceMarinesList.add(userSpaceMarines);
            }
        } catch (IOException e) {
            logger.error("Error reading file", e);
            answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "error", 0, "Error reading file");
            return ResponseEntity.status(422).body(answer);
        }

        try {
            // Сохраняем все координаты и главы
            for (int i = 0; i < spaceMarineList.size(); i++) {
                SpaceMarine spaceMarine = spaceMarineList.get(i);

                // Сохраняем координаты и главы
                Coordinates coordinates = coordinatesService.save(coordinatesList.get(i));
                Chapter chapter = chapterService.save(chapterList.get(i));

                // Обновляем SpaceMarine с новыми ID координат и главы
                spaceMarine.setCoordinatesId(coordinates.getId());
                spaceMarine.setChapterId(chapter.getId());
                spaceMarineService.save(spaceMarine);

                // Создаем объект ObjectHistory и привязываем к SpaceMarine
                ObjectHistory objectHistory = objectHistoryList.get(i);
                objectHistory.setSpaceMarineId(spaceMarine.getId());
                objectHistoryService.save(objectHistory);

                // Создаем объект UserSpaceMarines и связываем с SpaceMarine
                UserSpaceMarines userSpaceMarines = userSpaceMarinesList.get(i);
                userSpaceMarines.setSpaceMarineId(spaceMarine.getId());
                userSpaceMarinesService.save(userSpaceMarines);
            }
            answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "success", spaceMarineList.size(), "New spacemarines!");
            return ResponseEntity.status(200).body(answer);
        } catch (Exception e) {
            logger.error("Error processing import history", e);
            answer = importHistoryService.createImportHistory(userService.findById(userIdFromToken).getUsername(), "error", 0, "Error processing import history");
            return ResponseEntity.status(500).body(answer);
        }
    }

    // Пример эндпоинта для получения всех записей (можно добавить другие)
    @GetMapping("/table")
    public ResponseEntity<List<ImportHistory>> getAllImportHistory(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // предполагаем, что токен передается в формате "Bearer <token>"

        if (!authService.isTokenValid(token)) {
            return ResponseEntity.status(403).body(null); // Если токен не валиден, возвращаем ошибку 403
        }

        // Проверяем валидность токена и извлекаем роль и userId из токена
        Claims claims = authService.getClaimsFromToken(token);
        String role = claims.get("role", String.class); // Извлекаем роль из токена
        int userIdFromToken = claims.get("userId", Integer.class); // Извлекаем userId из токена

        // Получаем username по userId
        String username = userService.findById(userIdFromToken).getUsername();

        // Получаем все записи истории импорта
        List<ImportHistory> allImportHistory = importHistoryService.findAll();

        // Фильтруем записи в зависимости от роли
        List<ImportHistory> filteredImportHistory;

        if ("ADMIN".equals(role)) {
            // Если роль "ADMIN", показываем все записи
            filteredImportHistory = allImportHistory;
        } else {
            // Если роль не "ADMIN", показываем только записи для текущего пользователя
            filteredImportHistory = allImportHistory.stream()
                    .filter(importHistory -> importHistory.getUsername().equals(username))
                    .collect(Collectors.toList());
        }

        // Возвращаем отфильтрованные записи в ResponseEntity
        return new ResponseEntity<>(filteredImportHistory, HttpStatus.OK);
    }
}