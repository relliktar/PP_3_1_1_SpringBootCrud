package web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "web")
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * SpringResourceTemplateResolver делает фактическую работу по поиску вашего шаблона.
     * По существу, вы говорите (с помощью синтаксиса Spring Resources):
     * «Все мои шаблоны находятся по пути /WEB-INF/pages/».
     * И по умолчанию все они заканчиваются на .html.
     * <p>
     * Всякий раз, когда наш контроллер возвращает String, подобный index,
     * ThymeleafViewResolver будет искать шаблон: /WEB-INF/pages/index.html.
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    /**
     * Реализация ISpringTemplateEngine, предназначенная для приложений с поддержкой Spring,
     * которая по умолчанию устанавливает экземпляр SpringStandardDialect в качестве диалекта
     * (вместо экземпляра org.thymeleaf.standard.StandardDialect.)
     * SpringTemplateEngine автоматически применяет SpringStandardDialect и
     * включает собственные механизмы разрешения сообщений Spring MessageSource.
     * Включение компилятора SpringEL с Spring 4.2.4 или новее может
     * ускорять выполнение в большинстве сценариев, но может быть несовместимым
     * с особыми случаями повторного использования выражений в одном шаблоне
     * для разных типов данных, поэтому этот флаг по умолчанию "ложь"
     * для более безопасной обратной совместимости.
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /**
     * Метод используется для настройки преобразователя представления.
     * Параметр этого метода, ViewResolverRegistry, является регистратором,
     * используемым для регистрации преобразователя представления, который вы хотите настроить.
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}
