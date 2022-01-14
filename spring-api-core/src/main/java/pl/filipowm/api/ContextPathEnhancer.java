package pl.filipowm.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
class ContextPathEnhancer implements ApiDecorator, InitializingBean {

    private final String baseContext;

    @Override
    public void afterPropertiesSet() {
        log.info("Base context set to: {}", baseContext);
    }

    @Override
    public void decorate(ApiBuilder builder) {
        var apiContext = builder.getApi().baseContext();
        var context = StringUtils.hasLength(apiContext) ? apiContext : baseContext;
        if (StringUtils.hasLength(context)) {
            builder.getPathHolders().applyContext(context);
        }
    }

    @Override
    public boolean supports(ApiBuilder builder) {
        return builder.getApi() != null;
    }
}
