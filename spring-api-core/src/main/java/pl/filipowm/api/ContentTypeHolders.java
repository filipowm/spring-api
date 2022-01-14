package pl.filipowm.api;

import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import pl.filipowm.api.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
class ContentTypeHolders implements VersionTarget {

    private final List<ContentTypeHolder> consumes = new ArrayList<>();

    private final List<ContentTypeHolder> produces = new ArrayList<>();

    private String version;

    void applyVersion(String version) {
        ApiUtils.applyVersion(consumes, version);
        ApiUtils.applyVersion(produces, version);
    }

    Pair<ConsumesRequestCondition, ProducesRequestCondition> toCondition() {
        var consumesArr = apply(this.consumes);
        var producesArr = apply(this.produces);
        var consumesCondition = new ConsumesRequestCondition(consumesArr);
        var producesCondition = new ProducesRequestCondition(producesArr);
        return Pair.of(consumesCondition, producesCondition);
    }

    private String[] apply(Collection<ContentTypeHolder> contentTypeHolders) {
        return contentTypeHolders
                   .stream()
                   .map(ContentTypeHolder::toContentType)
                   .toArray(String[]::new);
    }

    void addConsumes(String consumes, String contentVnd) {
        this.consumes.add(ContentTypeHolder.of(consumes, contentVnd));
    }

    void addProduces(String produces, String contentVnd) {
        this.produces.add(ContentTypeHolder.of(produces, contentVnd));
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
