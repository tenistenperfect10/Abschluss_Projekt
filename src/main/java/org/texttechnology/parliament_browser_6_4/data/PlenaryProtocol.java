package org.texttechnology.parliament_browser_6_4.data;

import org.texttechnology.parliament_browser_6_4.exception.InputException;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface PlenaryProtocol extends PlenaryObject{

    /**
     * Return the sequence number fo the protocol
     * @return
     */
    int getIndex();

    /**
     * Set the sequence number fo the protocol
     * @param iValue
     */
    void setIndex(int iValue);

    /**
     * Return the date of the protocol
     * @return
     */
    Date getDate();

    /**
     * Return the formated Date
     * @return
     */
    String getDateFormated();

    /**
     * Set the date of the protocol
     * @param pDate
     */
    void setDate(Date pDate);

    /**
     * Return the start time of the protocol
     * @return
     */
    Timestamp getStartTime();


    /**
     * Return the start time formated
     * @return
     */
    String getStartTimeFormated() throws InputException;

    /**
     * Set the start time of the protocol
     * @param pTime
     */
    void setStarttime(Time pTime);

    /**
     * Return the end time of the protocol
     * @return
     */
    Timestamp getEndTime();

    /**
     * Return the end time formated
     * @return
     */
    String getEndTimeFormated() throws InputException;

    /**
     * Set the end time of the protocol
     * @param pTime
     */
    void setEndTime(Time pTime);

    /**
     * Return the title of the protocol
     * @return
     */
    String getTitle();

    /**
     * Set the title of the protocol
     * @param sValue
     */
    void setTitle(String sValue);

    /**
     * Return the agenda items of the protocol
     * @return
     */
    List<AgendaItem> getAgendaItems();

    /**
     * Add a agenda item
     * @param pItem
     */
    void addAgendaItem(AgendaItem pItem);

    /**
     * Add multiple agenda items
     * @param pSet
     */
    void addAgendaItems(Set<AgendaItem> pSet);

    /**
     * Return the place of the protocol
     * @return
     */
    String getPlace();

    /**
     * Set the place of the protocol
     * @param sValue
     */
    void setPlace(String sValue);

    /**
     * Return a list of speakers of the protocol
     * @return
     */
    Set<Speaker> getSpeakers();




}
