package com.github.ccguyka.showupdates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.google.common.collect.Lists;

public class FilterExcludedArtifacts {

    private final List<String> excludes;

    public FilterExcludedArtifacts(final String... excludes) {
        this.excludes = Lists.newArrayList(excludes);
    }

    public Map<Artifact, List<ArtifactVersion>> filter(final Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, List<ArtifactVersion>> filteredExcludedArtifacts = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            filteredExcludedArtifacts.put(update.getKey(), filterExcludedArtifacts(update.getValue()));
        }

        return filteredExcludedArtifacts;
    }

    private List<ArtifactVersion> filterExcludedArtifacts(final List<ArtifactVersion> update) {
        return update.stream().filter(filterExcludedArtifact()).collect(Collectors.toList());
    }

    private Predicate<? super ArtifactVersion> filterExcludedArtifact() {
        return artifact -> Lists.newArrayList(excludes).stream()
                .noneMatch(exclude -> artifact.toString().contains(exclude));
    }
}