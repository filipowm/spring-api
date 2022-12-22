package io.github.filipowm.api;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString
public class PathHolders implements VersionTarget {

    private final String pathPrefix;

    private final String versionPrefix;

    private final List<PathHolder> holders = new ArrayList<>();

    private String version;

    public void add(String path) {
        add(PathHolder.with(path, pathPrefix, versionPrefix));
    }

    public void add(PathHolder holder) {
        this.holders.add(holder);
    }

    public void applyApi(String api) {
        holders.forEach(holder -> holder.setApi(api));
    }

    public void applyContext(String context) {
        holders.forEach(holder -> holder.setContext(context));
    }

    public void applyVersion(String version) {
        ApiUtils.applyVersion(holders, version);
    }

    public String[] toPaths() {
        return holders.stream()
                      .map(PathHolder::toPath)
                      .toArray(String[]::new);
    }

    public org.springframework.web.reactive.result.condition.PatternsRequestCondition toReactiveCondition() {
        var parser = new PathPatternParser();
        var paths = holders.stream()
                           .map(PathHolder::toPath)
                           .map(parser::parse)
                           .collect(Collectors.toList());
        return new org.springframework.web.reactive.result.condition.PatternsRequestCondition(paths);
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public List<PathHolder> getHolders() {
        return holders;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        applyVersion(version);
        this.version = version;
    }
}
