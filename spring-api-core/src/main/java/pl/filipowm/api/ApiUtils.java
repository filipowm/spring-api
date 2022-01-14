package pl.filipowm.api;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static pl.filipowm.api.annotations.ApiVersion.UNVERSIONED;

@UtilityClass
class ApiUtils {

    static final String PATH_DELIMETER = "/";

    int extractVersionFromPath(String path, String versionPrefix) {
        int version = UNVERSIONED;
        if (!StringUtils.hasLength(path)) {
            return version;
        }
        var parts = path.split(PATH_DELIMETER);
        for (var pathPart : parts) {
            version = extractFromPrefix(pathPart, versionPrefix);
            if (version != UNVERSIONED) {
                break;
            }
        }
        return version;
    }

    private int extractFromPrefix(String versionString, String versionPrefix) {
        if (versionString.startsWith(versionPrefix)) {
            var numericVersion = versionString.substring(1);
            var builder = new StringBuilder();
            for (var c : numericVersion.toCharArray()) {
                if (c == '/') {
                    break;
                }
                builder.append(c);
            }
            var version = builder.toString();
            try {
                return Integer.parseInt(version);
            } catch (NumberFormatException e) {
                return UNVERSIONED;
            }
        }
        return UNVERSIONED;
    }

    int extractVersionFromContentType(String contentType, String contentVnd, String versionPrefix) {
        if (!StringUtils.hasLength(contentType) || !contentType.contains(contentVnd)) {
            return UNVERSIONED;
        }
        var splitted = contentType.split("\\.");
        var versionPart = Arrays.stream(splitted)
                                .filter(part -> part.startsWith(versionPrefix))
                                .findFirst()
                                .orElse(null);
        if (versionPart == null) {
            return UNVERSIONED;
        }
        var builder = new StringBuilder();
        for (var c : versionPart.toCharArray()) {
            if (c == '+') {
                break;
            }
            builder.append(c);
        }
        return extractFromPrefix(builder.toString(), versionPrefix);
    }

    static void applyVersion(List<? extends VersionTarget> targets, String version) {
        targets.forEach(target -> target.setVersion(version));
    }
}
