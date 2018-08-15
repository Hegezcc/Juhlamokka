/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.util.Date;

/**
 *
 * @author s1800580
 */
public abstract class DBObject {

    /**
     * Object id, used for database purposes (created by database)
     */
    protected Integer id;
    
    /**
     * A name for object, that can be used for example to search the object
     */
    protected String name;
    
    /**
     * A description text for that object
     */
    protected String description;

    /**
     * Timestamp of the very moment when that object was created in database (is set by database)
     */
    protected Date addedOn;

    /**
     * Timestamp of the moment when this object was modified in database (is set by database)
     */
    protected Date modifiedOn;
}
