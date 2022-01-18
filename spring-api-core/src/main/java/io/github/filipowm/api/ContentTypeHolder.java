package io.github.filipowm.api;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

@AllArgsConstructor
public final class ContentTypeHolder implements VersionTarget {

    private MediaType contentType;
    private final String contentVnd;
    private String version;

    String toContentType() {
        return contentType.toString();
    }

    public static ContentTypeHolder of(String contentType, String contentVnd) {
        return new ContentTypeHolder(MediaType.parseMediaType(contentType), contentVnd, null);
    }

    public static ContentTypeHolder of(MediaType contentType, String contentVnd) {
        return new ContentTypeHolder(contentType, contentVnd, null);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        if (!isVersioned() && StringUtils.hasLength(version)) {
            this.version = version;
            if (!contentType.getSubtype().contains(version)) {
                var newContentType = applyVersion(version);
                this.contentType = MediaType.parseMediaType(newContentType);
            }
        }
    }

    private String applyVersion(String version) {
        if (contentType.isConcrete() && !contentType.getSubtype().startsWith(contentVnd)) {
            var builder = new StringBuilder();
            builder.append(contentType.getType());
            builder.append("/");
            builder.append(version);
            builder.append("+");
            builder.append(contentType.getSubtype());

            for (var entry : contentType.getParameters().entrySet()) {
                builder.append(";");
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
            }
            return builder.toString();
        }
        return contentType.toString();
    }
}
