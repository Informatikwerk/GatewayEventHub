package de.informatikwerk.gatewayeventhub.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.domain.ConfigElement;
import de.informatikwerk.gatewayeventhub.repository.ConfigElementRepository;

/**
 * Service Implementation for managing ConfigElement.
 */
@Service
@Transactional
public class ConfigElementService {

    private final Logger log = LoggerFactory.getLogger(ConfigElementService.class);

    @Autowired
    ApplicationProperties applicationProperties;

    private final ConfigElementRepository configElementRepository;

    public ConfigElementService(ConfigElementRepository configElementRepository) {
        this.configElementRepository = configElementRepository;
    }

    /**
     * Diese Methode wird beim Application startup aufgerufen. Sie dient dem
     * automatischen Setzen von Application Properties durch ConfigElemente
     * (Key-Value-Paare), welche in der Datenbank liegen.
     */
    public void init() {
        // Zuerst werden alle bisher existierenden ConfigElemente geholt und es wird
        // eine Liste mit den Key Namen erstellt.
        List<ConfigElement> allConfigElements = configElementRepository.findAll();
        List<String> configElementKeys = new ArrayList<String>();
        log.debug("Found these config elements:");
        for (ConfigElement element : allConfigElements) {
            configElementKeys.add(element.getKey());
            log.debug("name:[" + element.getKey() + "], value:[" + element.getValue() + "]");
        }

        // Dann nimmt man sich alle Felder, die in den ApplicationProperties gelistet
        // sind und checkt, ob es auch für jede Property ein ConfigElement gibt.
        // Wenn ja, dann passiert hier erst einmal gar nichts
        // Wenn nein, dann wird ein neues ConfigElement erstellt, welches mit den
        // Standardwerten (aus der app.yml) befüllt wird.
        Field[] appConfigFields = ApplicationProperties.class.getDeclaredFields();
        log.debug("Found these application property fields:");
        for (Field field : appConfigFields) {
            String fieldName = field.getName();
            log.debug(fieldName + " (" + field.getType() + ")");
            if (configElementKeys.contains(fieldName)) {
                log.debug("ConfigElement for field [" + fieldName + "] is present");
            } else {
                log.debug("No ConfigElement found for field [" + fieldName + "], making a new one");
                makeNewConfigElementFromField(field);
            }
        }

        // Hier werden nun alle ConfigElemente genommen und die eingestellten werde
        // werden auf die Application angewendet.
        setApplicationPropertiesFromConfigElements(allConfigElements);
    }

    /**
     * Diese Methode erstellt ein neues ConfigElement, welches den Name des fields
     * trägt und mit dessen Standardwert befüllt wird
     * 
     * @param field
     */
    public void makeNewConfigElementFromField(Field field) {
        if (field != null) {
            String fieldName = field.getName();
            ConfigElement newElem = new ConfigElement();
            newElem.setId(null); // Id null -> neues Element
            newElem.setKey(fieldName);
            try {
                // Wir können den Wert des fields nur über den getter beziehen, also müssen wir
                // erst mal schauen ob es einen passenden Getter gibt.
                // Die Idee ist es, dass die ApplicationProperties.class einfach erweitert
                // werden kann, ohne dass hier etwas geändert werden muss.
                String getterName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
                log.debug("Checking if getter exists: " + getterName);
                Method getMethod = ApplicationProperties.class.getDeclaredMethod(getterName);
                Object methodInvokeResult = getMethod.invoke(applicationProperties);
                log.debug("Getter found for field [" + fieldName + "]");
                log.debug("Result: " + methodInvokeResult.toString());
                newElem.setValue(methodInvokeResult.toString());
                save(newElem);
            } catch (Exception e) {
                if (e.getClass() == NoSuchMethodException.class) {
                    log.warn("No getter found for field [" + fieldName + "]");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Alle ConfigElemente nehmen und deren Keys mit Values auf die
     * ApplicationProperties anwenden
     */
    public void setApplicationPropertiesFromConfigElements(List<ConfigElement> configElements) {
        for (ConfigElement configElement : configElements) {
            setApplicationPropertyForConfigElement(configElement);
        }
    }

    /**
     * Ein ConfigElement nehmen und dessen Werte (soweit möglich) auf die
     * ApplicationProperties anwenden
     * 
     * @param ce
     */
    public void setApplicationPropertyForConfigElement(ConfigElement ce) {
        if (ce != null) {
            String keyName = ce.getKey();
            log.debug("Setting Application Property [" + keyName + "] to value [" + ce.getValue() + "]");
            try {
                // Wir können den Wert des ConfigElements nur über den setter setzen, also
                // müssen wir erst mal schauen ob es einen passenden Setter gibt.
                // Die Idee ist es, dass die ApplicationProperties.class einfach erweitert
                // werden kann, ohne dass hier etwas geändert werden muss.
                String setterName = "set" + keyName.toUpperCase().charAt(0) + keyName.substring(1);
                log.debug("Checking if setter exists: " + setterName);
                Method getMethod = ApplicationProperties.class.getDeclaredMethod(setterName, String.class);
                getMethod.invoke(applicationProperties, ce.getValue());
                log.debug("Setter found for field [" + keyName + "]");
            } catch (Exception e) {
                if (e.getClass() == NoSuchMethodException.class) {
                    log.warn("No setter found for field [" + keyName + "]");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save a configElement.
     *
     * @param configElement the entity to save
     * @return the persisted entity
     */
    public ConfigElement save(ConfigElement configElement) {
        log.debug("Request to save ConfigElement : {}", configElement);
        if (configElement.getId() != null) {
            setApplicationPropertyForConfigElement(configElement);
        }
        return configElementRepository.save(configElement);
    }

    /**
     * Get all the configElements.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConfigElement> findAll() {
        log.debug("Request to get all ConfigElements");
        return configElementRepository.findAll();
    }

    /**
     * Get one configElement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ConfigElement findOne(Long id) {
        log.debug("Request to get ConfigElement : {}", id);
        return configElementRepository.findOne(id);
    }

    /**
     * Delete the configElement by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigElement : {}", id);
        configElementRepository.delete(id);
    }
}
