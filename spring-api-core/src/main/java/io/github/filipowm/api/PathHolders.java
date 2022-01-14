package io.github.filipowm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class PathHolders implements VersionTarget {

    private final String pathPrefix;
    private final String versionPrefix;
    private final List<PathHolder> holders = new ArrayList<>();
    private String version;

    void add(String path) {
        add(PathHolder.with(path, pathPrefix, versionPrefix));
    }

    void add(PathHolder holder) {
        this.holders.add(holder);
    }

    void applyApi(String api) {
        holders.forEach(holder -> holder.setApi(api));
    }

    void applyContext(String context) {
        holders.forEach(holder -> holder.setContext(context));
    }

    void applyVersion(String version) {
        ApiUtils.applyVersion(holders, version);
    }

    PatternsRequestCondition toCondition() {
        var paths = holders.stream()
                .map(PathHolder::toPath)
                .toArray(String[]::new);
        return new PatternsRequestCondition(paths);
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
