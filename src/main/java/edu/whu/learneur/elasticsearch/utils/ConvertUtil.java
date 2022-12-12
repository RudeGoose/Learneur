package edu.whu.learneur.elasticsearch.utils;

import edu.whu.learneur.elasticsearch.service.ResourceSearchService.ResourceType;
import edu.whu.learneur.resource.entity.*;
import edu.whu.learneur.elasticsearch.entity.ResourceEs;

public class ConvertUtil {
    private static ResourceEs typeAndIdAndNameAndDescriptionToEs(Short resourceType, Long resourceId, String name, String description) {
        return ResourceEs.builder()
                .resourceType(resourceType)
                .resourceId(resourceId)
                .name(name)
                .description(description)
                .build();
    }

    public static ResourceEs BookToResourceEs(Book book) {
        return typeAndIdAndNameAndDescriptionToEs(ResourceType.Book, book.getId(), book.getTitle(), null);
    }

    public static ResourceEs LessonToResourceEs(Lesson lesson) {
        return typeAndIdAndNameAndDescriptionToEs(ResourceType.Lesson, lesson.getId(), lesson.getTitle(), lesson.getDescription());
    }

    public static ResourceEs ProjectToResourceEs(Project project) {
        return typeAndIdAndNameAndDescriptionToEs(ResourceType.Project, project.getIdProject(), project.getName(), project.getDescription());
    }

    public static ResourceEs TutorialToResourceEs(Tutorial tutorial) {
        return typeAndIdAndNameAndDescriptionToEs(ResourceType.Tutorial, tutorial.getId(), tutorial.getName(), tutorial.getSummary());
    }

    public static ResourceEs VideoToResourceEs(Video video) {
        return typeAndIdAndNameAndDescriptionToEs(ResourceType.Video, video.getId(), video.getTitle(), video.getDescription());
    }


}
