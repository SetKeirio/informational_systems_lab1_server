package informational_systems.lab1.controllers;

import informational_systems.lab1.items.Chapter;
import informational_systems.lab1.items.ChapterResponse;
import informational_systems.lab1.items.SpaceMarineResponse;
import informational_systems.lab1.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chapters")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    @GetMapping("/table")
    public ResponseEntity<List<ChapterResponse>> getAllChapters() {
        List<Chapter> chapters = chapterService.findAll();
        List<ChapterResponse> answer = new ArrayList<ChapterResponse>();
        for (int i = 0; i < chapters.size(); i++){
            ChapterResponse temp = new ChapterResponse();
            temp.setId(chapters.get(i).getId());
            temp.setName(chapters.get(i).getName());
            temp.setMarinesCount(chapters.get(i).getMarinesCount());
            answer.add(temp);
        }
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/create")
    public Chapter createChapter(@RequestBody Chapter chapter) {
        return chapterService.save(chapter);
    }
}