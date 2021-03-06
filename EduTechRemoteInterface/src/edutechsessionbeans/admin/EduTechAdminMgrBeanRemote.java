/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edutechsessionbeans.admin;

import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Derian
 */
@Remote
public interface EduTechAdminMgrBeanRemote {

    public Object getModuleCount();

    public Object getSemesterCount();

    public boolean createSemester(String parameter, String parameter0, String parameter1);

    public ArrayList getAllSemesters();

    public ArrayList getSemesterInfo(String id);

    public void deleteSemester(String id);

    public boolean editSemester(String parameter, String parameter0, String parameter1, String parameter3);

    public boolean createModule(String moduleCode, String name, Long modularCredit, String description, Long semID);

    public ArrayList getAllModules();

    public void deleteModule(String id);

    public ArrayList getModuleInfo(String id);

    public boolean editModule(String id, String name, String credits, String description, String semID);

    public boolean addEventToMod(String title, String location, String day, String startTime, String endTime, String description, String id);

    public ArrayList getAllModulesOfUser(String id);

    public boolean addUserToMod(String id, String mod);

    public void unassignModule(String id, String mod);

    public ArrayList getCurrentSemester();

    public void removeEventFromMod(String parameter, String id);

    public ArrayList getModuleRecurringEvents(String id);
    
}
