package pl.filipowm.api;

/**
 * Objects implementing this interface must be responsible
 * for customizing API handled by {@link ApiRequestMappingHandlerMapping}.
 *
 * If you want to customize how API is built, simply implement
 * this interface and register implementation as a Spring Bean. It will be then
 * automatically wired to request mapping engine.
 */
public interface ApiDecorator {

    /**
     * Method responsible for customizing API.
     * Operate indirectly on {@link ApiBuilder},
     * which then is used to build final mapping definition
     *
     * @param builder API builder
     */
    void decorate(ApiBuilder builder);

    /**
     * Control which API endpoints should be handled by this customizer.
     * Return <i>true</i> to handle all API endpoints.
     * @param builder API builder
     * @return true, if this object should be used to customize API, otherwise false
     */
    boolean supports(ApiBuilder builder);

}
