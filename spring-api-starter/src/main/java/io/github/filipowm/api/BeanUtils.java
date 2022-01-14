package io.github.filipowm.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

/**
 * Internally-used Spring Beans utilities
 */
public class BeanUtils {

    private BeanUtils() {
    }

    /**
     * Find bean in {@link ApplicationContext} without throwing exception if not found
     *
     * @param context application context
     * @param clss    bean type to be found
     * @param <T>     bean type
     * @return empty optional, if bean not found, otherwise bean wrapped in optional object
     */
    public static <T> Optional<T> getBean(ApplicationContext context, Class<? extends T> clss) {
        T bean = null;
        try {
            bean = context.getBean(clss);
        } catch (BeansException e) {
            //discard
        }

        return Optional.ofNullable(bean);
    }
}
