package com.github.pannowak.mealsadvisor.core.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.exception.EntityNotFoundException;
import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;
import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import com.github.pannowak.mealsadvisor.api.internationalization.MessageDictionary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.core.context.LocaleContextManager;
import org.hibernate.PersistentObjectException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExceptionFactory {

    private static final String EXCEPTION_BUNDLE_NAME = ExceptionFactory.class.getPackageName() + ".ExceptionMessages";

    private static final String SINGLE_FETCH_EXCEPTION_KEY = "singleFetchException";
    private static final String MULTI_FETCH_EXCEPTION_KEY = "multiFetchException";
    private static final String SAVING_EXCEPTION_KEY = "savingException";
    private static final String REMOVAL_EXCEPTION_KEY = "removalException";
    private static final String NOT_FOUND_EXCEPTION_KEY = "notFoundException";
    private static final String ILLEGAL_WEEK_DAY_EXCEPTION_KEY = "illegalWeekDayException";
    private static final String INSUFFICIENT_DATA_EXCEPTION_KEY = "insufficientDataException";
    private static final String DATA_INTEGRITY_EXCEPTION_KEY = "dataIntegrityException";
    private static final String NON_TRANSIENT_ENTITY_EXCEPTION_KEY = "nonTransientEntityException";
    private static final String VALIDATION_EXCEPTION_KEY = "validationException";
    private static final String VALIDATION_MESSAGE_KEY = "validationMessage";

    private final LocaleContextManager localeContextManager;

    ExceptionFactory(LocaleContextManager localeContextManager) {
        this.localeContextManager = localeContextManager;
    }

    public ServiceException singleFetchException(Class<?> entityType, Long entityId, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();

        String entityName = entityType.getSimpleName();
        String message = messageDictionary.getMessage(SINGLE_FETCH_EXCEPTION_KEY, entityName, entityId);
        String localizedMessage = messageDictionary.getLocalizedMessage(SINGLE_FETCH_EXCEPTION_KEY, entityName, entityId);
        return getAdjustedException(new DatabaseException(message, localizedMessage, cause));
    }

    public ServiceException multiFetchException(Class<?> entityType, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();

        String entityName = entityType.getSimpleName();
        String message = messageDictionary.getMessage(MULTI_FETCH_EXCEPTION_KEY, entityName);
        String localizedMessage = messageDictionary.getLocalizedMessage(MULTI_FETCH_EXCEPTION_KEY, entityName);
        return getAdjustedException(new DatabaseException(message, localizedMessage, cause));
    }

    public MealsAdvisorException savingException(Object entity, Throwable cause) {
        var savingException = translateToSavingException(entity, cause);
        return getAdjustedException(savingException, 3);
    }

    public ServiceException removalException(Class<?> entityType, Long entityId, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();

        String entityName = entityType.getSimpleName();
        String message = messageDictionary.getMessage(REMOVAL_EXCEPTION_KEY, entityName, entityId);
        String localizedMessage = messageDictionary.getLocalizedMessage(REMOVAL_EXCEPTION_KEY, entityName, entityId);
        return getAdjustedException(new DatabaseException(message, localizedMessage, cause));
    }

    public EntityNotFoundException entityNotFoundException(Class<?> entityType, Long entityId) {
        var messageDictionary = getCurrentMessageDictionary();

        String entityName = entityType.getSimpleName();
        String message = messageDictionary.getMessage(NOT_FOUND_EXCEPTION_KEY, entityName, entityId);
        String localizedMessage = messageDictionary.getLocalizedMessage(NOT_FOUND_EXCEPTION_KEY, entityName, entityId);
        return getAdjustedException(new EntityNotFoundInDatabaseException(message, localizedMessage));
    }

    public ClientException illegalWeekDayException(int illegalDayOfWeek) {
        var messageDictionary = getCurrentMessageDictionary();

        String message = messageDictionary.getMessage(ILLEGAL_WEEK_DAY_EXCEPTION_KEY, illegalDayOfWeek);
        String localizedMessage = messageDictionary.getLocalizedMessage(ILLEGAL_WEEK_DAY_EXCEPTION_KEY, illegalDayOfWeek);
        return getAdjustedException(new IllegalWeekDayException(message, localizedMessage));
    }

    public ClientException insufficientDataException(MealType missingMealsType) {
        var messageDictionary = getCurrentMessageDictionary();

        var mealTypeName = missingMealsType.getName();
        String message = messageDictionary.getMessage(INSUFFICIENT_DATA_EXCEPTION_KEY, mealTypeName);
        String localizedMessage = messageDictionary.getLocalizedMessage(INSUFFICIENT_DATA_EXCEPTION_KEY, mealTypeName);
        return getAdjustedException(new InsufficientDataException(message, localizedMessage));
    }

    private <T extends MealsAdvisorException> T getAdjustedException(T exception) {
        return getAdjustedException(exception, 1);
    }

    private <T extends MealsAdvisorException> T getAdjustedException(T exception,
                                                                     int maxNumberElementsToRemove) {
        StackTraceElement[] newStackTrace = removeFactoryMethodCallFromStackTrace(
                exception.getStackTrace(), maxNumberElementsToRemove);
        exception.setStackTrace(newStackTrace);
        return exception;
    }

    private StackTraceElement[] removeFactoryMethodCallFromStackTrace(StackTraceElement[] oldStackTrace,
                                                                      int maxNumberElementsToRemove) {
        int oldStackLength = oldStackTrace.length;
        int numberOfElementsToRemove = Math.min(maxNumberElementsToRemove, oldStackLength);

        int newStackLength = oldStackLength - numberOfElementsToRemove;
        StackTraceElement[] newStackTrace = new StackTraceElement[newStackLength];
        System.arraycopy(oldStackTrace, numberOfElementsToRemove, newStackTrace, 0, newStackLength);

        return newStackTrace;
    }

    private MealsAdvisorException translateToSavingException(Object entityToSave, Throwable cause) {
        var rootCause = NestedExceptionUtils.getMostSpecificCause(cause);
        if (rootCause instanceof SQLIntegrityConstraintViolationException) {
            return dataIntegrityException(entityToSave, cause);
        } else if (rootCause instanceof PersistentObjectException) {
            String entityType = extractNonTransientEntityType((PersistentObjectException) rootCause);
            return nonTransientEntityException(entityType, cause);
        } else {
            return databaseSavingException(entityToSave, cause);
        }
    }

    private ClientException dataIntegrityException(Object entity, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();
        String message = messageDictionary.getMessage(DATA_INTEGRITY_EXCEPTION_KEY, entity);
        String localizedMessage = messageDictionary.getLocalizedMessage(DATA_INTEGRITY_EXCEPTION_KEY, entity);
        return new DataIntegrityException(message, localizedMessage, cause);
    }

    private ClientException nonTransientEntityException(String entityType, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();
        String message = messageDictionary.getMessage(NON_TRANSIENT_ENTITY_EXCEPTION_KEY, entityType);
        String localizedMessage = messageDictionary.getLocalizedMessage(NON_TRANSIENT_ENTITY_EXCEPTION_KEY, entityType);
        return new NonTransientEntityException(message, localizedMessage, cause);
    }

    private String extractNonTransientEntityType(PersistentObjectException e) {
        var message = e.getMessage();
        return message.substring(message.lastIndexOf("."));
    }

    public ClientException validationException(ConstraintViolationException cause) {
        String message = getValidationMessage(cause.getConstraintViolations());
        String localizedMessage = getLocalizedValidationMessage(cause.getConstraintViolations());
        return getAdjustedException(new ValidationException(message, localizedMessage, cause));
    }

    private String getValidationMessage(Set<ConstraintViolation<?>> violations) {
        var messageDictionary = getCurrentMessageDictionary();
        String prefix = messageDictionary.getMessage(VALIDATION_EXCEPTION_KEY, "someType");
        String validationMessages = violations.stream()
                .map(violation -> messageDictionary.getMessage(VALIDATION_MESSAGE_KEY, violation.getPropertyPath(),
                        violation.getLeafBean().getClass().getSimpleName(), violation.getMessage()))
                .collect(Collectors.joining("\n\t", "\n\t", ""));
        return prefix + validationMessages;
    }

    private String getLocalizedValidationMessage(Set<ConstraintViolation<?>> violations) {
        var messageDictionary = getCurrentMessageDictionary();
        String prefix = messageDictionary.getLocalizedMessage(VALIDATION_EXCEPTION_KEY, "someType");
        String validationMessages = violations.stream()
                .map(violation -> messageDictionary.getLocalizedMessage(VALIDATION_MESSAGE_KEY,
                        violation.getPropertyPath(), violation.getLeafBean().getClass().getSimpleName(), violation.getMessage()))
                .collect(Collectors.joining("\n\t", "\n\t", ""));
        return prefix + validationMessages;
    }

    private ServiceException databaseSavingException(Object entity, Throwable cause) {
        var messageDictionary = getCurrentMessageDictionary();
        String message = messageDictionary.getMessage(SAVING_EXCEPTION_KEY, entity);
        String localizedMessage = messageDictionary.getLocalizedMessage(SAVING_EXCEPTION_KEY, entity);
        return new DatabaseException(message, localizedMessage, cause);
    }

    private MessageDictionary getCurrentMessageDictionary() {
        var currentLocale = localeContextManager.getCurrentLocale();
        return new MessageDictionary(EXCEPTION_BUNDLE_NAME, currentLocale);
    }
}
