package informational_systems.lab1.services;

import informational_systems.lab1.items.Chapter;
import informational_systems.lab1.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter save(Chapter chapter) {
        return chapterRepository.save(chapter);
    }
}