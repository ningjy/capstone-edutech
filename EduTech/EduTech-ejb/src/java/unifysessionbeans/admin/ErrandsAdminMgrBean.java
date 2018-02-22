/**
 * *************************************************************************************
 *   Title:                  ErrandsAdminMgrBean.java
 *   Purpose:                LIST OF MANAGER BEAN METHODS FOR UNIFY ERRANDS - ADMIN (EDUBOX)
 *   Created By:             CHEN MENG
 *   Modified By:            TAN CHIN WEE WINSTON
 *   Credits:                CHEN MENG, NIGEL LEE TJON YI, TAN CHIN WEE WINSTON, ZHU XINYI
 *   Date:                   21 FEBRUARY 2018
 *   Code version:           1.1
 *   Availability:           === NO REPLICATE ALLOWED. YOU HAVE BEEN WARNED. ===
 **************************************************************************************
 */
package unifysessionbeans.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import unifyentities.common.CategoryEntity;
import unifyentities.errands.JobEntity;
import unifyentities.errands.JobReviewEntity;
import unifyentities.errands.JobTransactionEntity;

@Stateless
public class ErrandsAdminMgrBean implements ErrandsAdminMgrBeanRemote {

    @PersistenceContext
    private EntityManager em;

    private JobEntity jEntity;
    private CategoryEntity cEntity;
    private Collection<JobEntity> jobSet;
    private Collection<JobReviewEntity> jobReviewSet;
    private Collection<JobTransactionEntity> jobTransactionSet;

    @Override
    public List<Vector> getAllJobCategory() {
        Query q = em.createQuery("SELECT c FROM Category c WHERE c.categoryType = 'Errands'");
        List<Vector> jobCategoryList = new ArrayList<Vector>();

        for (Object o : q.getResultList()) {
            CategoryEntity categoryE = (CategoryEntity) o;
            Vector jobCategoryVec = new Vector();

            jobCategoryVec.add(categoryE.getCategoryID());
            jobCategoryVec.add(categoryE.getCategoryImage());
            jobCategoryVec.add(categoryE.getCategoryName());
            jobCategoryVec.add(categoryE.getCategoryDescription());
            jobCategoryVec.add(categoryE.getCategoryActiveStatus());
            jobCategoryList.add(jobCategoryVec);
        }
        return jobCategoryList;
    }

    @Override
    public String createJobCategory(String categoryName, String categoryType, String categoryDescription, String categoryImage) {
        cEntity = new CategoryEntity();
        if (cEntity.createCategory(categoryName, categoryType, categoryDescription, categoryImage)) {
            em.persist(cEntity);
            return "Job category has been created successfully!";
        } else {
            return "There were some issues encountered while creating the job category. Please try again.";
        }
    }

    @Override
    public Vector getJobCategoryDetails(long jobCategoryID) {
        cEntity = lookupJobCategory(jobCategoryID);
        Vector jobCategoryDetailsVec = new Vector();

        if (cEntity != null) {
            jobCategoryDetailsVec.add(cEntity.getCategoryImage());
            jobCategoryDetailsVec.add(cEntity.getCategoryName());
            jobCategoryDetailsVec.add(cEntity.getCategoryType());
            jobCategoryDetailsVec.add(cEntity.getCategoryDescription());
            /* CATEGORY ACTIVE STATUS IS DEFAULT TRUE */
            if (cEntity.getCategoryActiveStatus()) {
                jobCategoryDetailsVec.add("Active");
            } else {
                jobCategoryDetailsVec.add("Inactive");
            }
        }
        return jobCategoryDetailsVec;
    }

    @Override
    public List<Vector> viewAssociatedJobList(long jobCategoryID) {
        cEntity = lookupJobCategory(jobCategoryID);
        List<Vector> jobList = new ArrayList<Vector>();

        if (cEntity.getItemSet() != null) {
            jobSet = cEntity.getJobSet();
            if (!jobSet.isEmpty()) {
                for (JobEntity je : jobSet) {
                    Vector jobDetails = new Vector();
                    
                    jobDetails.add(je.getJobID());
                    jobDetails.add(je.getCategoryEntity().getCategoryID());
                    jobDetails.add(je.getJobImage());
                    jobDetails.add(je.getJobTitle());
                    jobDetails.add(je.getUserEntity().getUsername());
                    jobDetails.add(je.getJobRate());
                    jobDetails.add(je.getJobRateType());
                    jobDetails.add(je.getJobWorkDate());
                    jobDetails.add(je.getJobStatus());
                    jobList.add(jobDetails);
                }
            } else {
                Query q = em.createQuery("SELECT j FROM Job j WHERE j.categoryEntity.categoryName = :jobCategoryName");
                q.setParameter("jobCategoryName", cEntity.getCategoryName());

                for (Object o : q.getResultList()) {
                    JobEntity jobE = (JobEntity) o;
                    Vector jobVec = new Vector();

                    jobVec.add(jobE.getJobImage());
                    jobVec.add(jobE.getJobTitle());
                    jobVec.add(jobE.getUserEntity().getUsername());
                    jobVec.add(jobE.getJobRate());
                    jobVec.add(jobE.getJobRateType());
                    jobVec.add(jobE.getJobWorkDate());
                    jobVec.add(jobE.getJobStatus());
                    jobList.add(jobVec);
                }
            }
        }
        return jobList;
    }

    @Override
    public String updateJobCategory(long jobCategoryID, String categoryName, String categoryDescription, String fileName) {
        /* DOES NOT MATTER WHETHER OR NOT THERE IS JOBS INSIDE THE JOB CATEGORY, ADMIN CAN JUST UPDATE THE JOB CATEGORY DETAILS */
        if (lookupJobCategory(jobCategoryID) == null) {
            return "Selected job category cannot be found. Please try again.";
        } else {
            cEntity = lookupJobCategory(jobCategoryID);
            cEntity.setCategoryName(categoryName);
            cEntity.setCategoryDescription(categoryDescription);
            cEntity.setCategoryImage(fileName);
            em.merge(cEntity);
            return "Selected job category has been updated successfully!";
        }
    }

    @Override
    public String deactivateAJobCategory(long jobCategoryID) {
        /* DON'T CHANGE THE RETURN STRING (USED FOR SERVLET VALIDATION) */
        boolean jobAvailWithinCat = false;

        if (lookupJobCategory(jobCategoryID) == null) {
            return "Selected job category cannot be found.";
        } else {
            cEntity = em.find(CategoryEntity.class, lookupJobCategory(jobCategoryID).getCategoryID());
            jobSet = cEntity.getJobSet();

            /* IF THERE ARE JOBS INSIDE THE JOB CATEGORY */
            if (!jobSet.isEmpty()) {
                em.clear();
                for (JobEntity je : jobSet) {
                    /* IF THE JOB INSIDE THE JOB CATEGORY IS "AVAILABLE", THEN CANNOT DEACTIVATE THE JOB CATEGORY */
                    if ((je.getJobStatus()).equals("Available")) {
                        jobAvailWithinCat = true;
                        break;
                    }
                }
                if (jobAvailWithinCat == true) {
                    return "There are active jobs currently inside this job category. Cannot be deactivated.";
                } else {
                    return "Selected job category has been deactivated successfully!";
                }
            } /* IF THERE ARE NO JOBS INSIDE THE JOB CATEGORY, PROCEED TO DEACTIVATE THE JOB CATEGORY */ else {
                cEntity.setCategoryActiveStatus(false);
                em.merge(cEntity);
                return "Selected job category has been deactivated successfully!";
            }
        }
    }

    @Override
    public String activateAJobCategory(long jobCategoryID) {
        if (lookupJobCategory(jobCategoryID) == null) {
            return "Selected job category cannot be found. Please try again.";
        } else {
            cEntity = lookupJobCategory(jobCategoryID);
            cEntity.setCategoryActiveStatus(true);
            em.merge(cEntity);
            return "Selected job category has been activated successfully!";
        }
    }
    
    @Override
    public List<Vector> getAllJobListing() {
        Query q = em.createQuery("SELECT j FROM Job j");
        List<Vector> jobList = new ArrayList<Vector>();

        for (Object o : q.getResultList()) {
            JobEntity jobE = (JobEntity) o;
            Vector jobVec = new Vector();

            jobVec.add(jobE.getJobID());
            jobVec.add(jobE.getJobImage());
            jobVec.add(jobE.getJobTitle());
            jobVec.add(jobE.getCategoryEntity().getCategoryName());
            jobVec.add(jobE.getUserEntity().getUsername());
            jobVec.add(jobE.getJobTakerID());
            jobVec.add(jobE.getJobRate());
            jobVec.add(jobE.getJobRateType());
            jobVec.add(jobE.getJobStatus());
            jobList.add(jobVec);
        }
        return jobList;
    }

    @Override
    public Vector getJobDetails(long jobID) {
        jEntity = lookupJob(jobID);
        Vector jobDetailsVec = new Vector();

        if (jEntity != null) {
            jobDetailsVec.add(jEntity.getJobImage());
            jobDetailsVec.add(jEntity.getJobTitle());
            jobDetailsVec.add(jEntity.getCategoryEntity().getCategoryName());
            jobDetailsVec.add(jEntity.getUserEntity().getUsername());
            jobDetailsVec.add(jEntity.getJobRate());
            jobDetailsVec.add(jEntity.getJobRateType());
            jobDetailsVec.add(jEntity.getJobDescription());
            jobDetailsVec.add(jEntity.getJobStatus());
            jobDetailsVec.add(jEntity.getJobNumOfLikes());
            jobDetailsVec.add(jEntity.getJobPostDate());
            jobDetailsVec.add(jEntity.getJobWorkDate());
            jobDetailsVec.add(jEntity.getJobStartLocation());
            jobDetailsVec.add(jEntity.getJobStartLat());
            jobDetailsVec.add(jEntity.getJobStartLong());
            jobDetailsVec.add(jEntity.getJobEndLocation());
            jobDetailsVec.add(jEntity.getJobEndLat());
            jobDetailsVec.add(jEntity.getJobEndLong());
            jobDetailsVec.add(jEntity.getJobInformation());
        }
        return jobDetailsVec;
    }

    @Override
    public List<Vector> getJobTransactionList(long jobID) {
        jEntity = lookupJob(jobID);
        List<Vector> jobTransactionList = new ArrayList<Vector>();

        if (jEntity.getJobTransactionSet() != null) {
            jobTransactionSet = jEntity.getJobTransactionSet();
            if (!jobTransactionSet.isEmpty()) {
                for (JobTransactionEntity jte : jobTransactionSet) {
                    Vector jobTransDetails = new Vector();
                    
                    jobTransDetails.add(jte.getJobTransactionDate());
                    /* WE ASSUME THAT THE JOB POSTER IS THE ONE WHO CREATES THE TRANSACTION */
                    jobTransDetails.add(jte.getUserEntity().getUsername());
                    jobTransDetails.add(jte.getJobTakerID());
                    jobTransDetails.add(jte.getJobTransactionRate());
                    jobTransDetails.add(jte.getJobTransactionRateType());
                    jobTransactionList.add(jobTransDetails);
                }
            } else {
                Query q = em.createQuery("SELECT t FROM JobTransaction t WHERE t.jobEntity.jobID = :jobID");
                q.setParameter("jobID", jEntity.getJobID());

                for (Object o : q.getResultList()) {
                    JobTransactionEntity jobTransE = (JobTransactionEntity) o;
                    Vector jobTransVec = new Vector();

                    jobTransVec.add(jobTransE.getJobTransactionDate());
                    /* WE ASSUME THAT THE JOB POSTER IS THE ONE WHO CREATES THE TRANSACTION */
                    jobTransVec.add(jobTransE.getUserEntity().getUsername());
                    jobTransVec.add(jobTransE.getJobTakerID());
                    jobTransVec.add(jobTransE.getJobTransactionRate());
                    jobTransVec.add(jobTransE.getJobTransactionRateType());
                    jobTransactionList.add(jobTransVec);
                }
            }
        }
        return jobTransactionList;
    }
    
    @Override
    public List<Vector> getJobReviewList(long jobID) {
        jEntity = lookupJob(jobID);
        List<Vector> jobReviewList = new ArrayList<Vector>();

        if (jEntity.getJobReviewSet() != null) {
            jobReviewSet = jEntity.getJobReviewSet();
            if (!jobReviewSet.isEmpty()) {
                for (JobReviewEntity jre : jobReviewSet) {
                    Vector jobReviewDetails = new Vector();
                    
                    jobReviewDetails.add(jre.getJobReviewDate());
                    jobReviewDetails.add(jre.getUserEntity().getUsername());
                    jobReviewDetails.add(jre.getJobReceiverID());
                    jobReviewDetails.add(jre.getJobReviewRating());
                    jobReviewDetails.add(jre.getJobReviewContent());
                    jobReviewList.add(jobReviewDetails);
                }
            } else {
                Query q = em.createQuery("SELECT r FROM JobReview r WHERE r.jobEntity.jobID = :jobID");
                q.setParameter("jobID", jEntity.getJobID());

                for (Object o : q.getResultList()) {
                    JobReviewEntity jobReviewE = (JobReviewEntity) o;
                    Vector jobReviewVec = new Vector();

                    jobReviewVec.add(jobReviewE.getJobReviewDate());
                    jobReviewVec.add(jobReviewE.getUserEntity().getUsername());
                    jobReviewVec.add(jobReviewE.getJobReceiverID());
                    jobReviewVec.add(jobReviewE.getJobReviewRating());
                    jobReviewVec.add(jobReviewE.getJobReviewContent());
                    jobReviewList.add(jobReviewVec);
                }
            }
        }
        return jobReviewList;
    }

    @Override
    public String deleteAJob(long jobID) {
        /* ADMIN CAN JUST DELETE THE JOB IMMEDIATELY */
        if (lookupJob(jobID) == null) {
            return "Selected job cannot be found. Please try again.";
        } else {
            jEntity = lookupJob(jobID);
            em.remove(jEntity);
            em.flush();
            em.clear();
            return "Selected job has been deleted successfully (A system notification has been sent to the job poster)!";
        }
    }

    /* STANDALONE PAGE JOB TRANSACTIONS */
    @Override
    public ArrayList<Vector> getAllJobTransactions() {
        Query q = em.createQuery("SELECT t FROM JobTransaction t");
        ArrayList<Vector> jobTransactionList = new ArrayList<Vector>();

        for (Object o : q.getResultList()) {
            JobTransactionEntity jobTransE = (JobTransactionEntity) o;
            Vector jobTransVec = new Vector();

            jobTransVec.add(jobTransE.getJobTransactionDate());
            /* WE ASSUME THAT THE JOB POSTER IS THE ONE WHO CREATES THE TRANSACTION */
            jobTransVec.add(jobTransE.getUserEntity().getUsername());
            jobTransVec.add(jobTransE.getJobTakerID());
            jobTransVec.add(jobTransE.getJobEntity().getJobTitle());
            jobTransVec.add(jobTransE.getJobEntity().getJobRate());
            jobTransVec.add(jobTransE.getJobEntity().getJobRateType());
            jobTransVec.add(jobTransE.getJobTransactionRate());
            jobTransVec.add(jobTransE.getJobTransactionRateType());

            jobTransactionList.add(jobTransVec);
        }
        return jobTransactionList;
    }

    /* METHODS FOR UNIFY ADMIN DASHBOARD */
    @Override
    public Long getErrandsTransTodayCount() {
        Long errandsTransTodayCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(t.jobTransactionID) FROM JobTransaction t");
        try {
            errandsTransTodayCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getErrandsTransTodayCount().getSingleResult()");
            ex.printStackTrace();
        }
        return errandsTransTodayCount;
    }
    
    @Override
    public Long getErrandsTransCount() {
        Long errandsTransCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(t.jobTransactionID) FROM JobTransaction t");
        try {
            errandsTransCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getErrandsTransCount().getSingleResult()");
            ex.printStackTrace();
        }
        return errandsTransCount;
    }

    @Override
    public Long getActiveJobCategoryListCount() {
        Long activeJobCategoryListCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(DISTINCT c.categoryName) FROM Category c WHERE c.categoryType = 'Errands' AND c.categoryActiveStatus='1'");
        try {
            activeJobCategoryListCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getActiveJobCategoryListCount().getSingleResult()");
            ex.printStackTrace();
        }
        return activeJobCategoryListCount;
    }

    @Override
    public Long getInactiveJobCategoryListCount() {
        Long inactiveJobCategoryListCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(DISTINCT c.categoryName) FROM Category c WHERE c.categoryType = 'Errands' AND c.categoryActiveStatus='0'");
        try {
            inactiveJobCategoryListCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getInactiveJobCategoryListCount().getSingleResult()");
            ex.printStackTrace();
        }
        return inactiveJobCategoryListCount;
    }

    @Override
    public Long getJobListingCount() {
        Long jobListingCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(j.jobID) FROM Job j");
        try {
            jobListingCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getJobListingCount().getSingleResult()");
            ex.printStackTrace();
        }
        return jobListingCount;
    }

    @Override
    public Long getAvailableJobListingCount() {
        Long availableJobListingCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(j.jobID) FROM Job j WHERE j.jobStatus = 'Available'");
        try {
            availableJobListingCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getAvailableJobListingCount().getSingleResult()");
            ex.printStackTrace();
        }
        return availableJobListingCount;
    }

    @Override
    public Long getReservedJobListingCount() {
        Long reservedJobListingCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(j.jobID) FROM Job j WHERE j.jobStatus = 'Reserved'");
        try {
            reservedJobListingCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getReservedJobListingCount().getSingleResult()");
            ex.printStackTrace();
        }
        return reservedJobListingCount;
    }

    @Override
    public Long getCompletedJobListingCount() {
        Long completedJobListingCount = new Long(0);
        Query q = em.createQuery("SELECT COUNT(j.jobID) FROM Job j WHERE j.jobStatus = 'Completed'");
        try {
            completedJobListingCount = (Long) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception in ErrandsAdminMgrBean.getCompletedJobListingCount().getSingleResult()");
            ex.printStackTrace();
        }
        return completedJobListingCount;
    }

    /* MISCELLANEOUS METHODS */
    public JobEntity lookupJob(long jobID) {
        JobEntity je = new JobEntity();
        try {
            Query q = em.createQuery("SELECT j FROM Job j WHERE j.jobID = :jobID");
            q.setParameter("jobID", jobID);
            je = (JobEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("ERROR: Job cannot be found. " + enfe.getMessage());
            em.remove(je);
            je = null;
        } catch (NoResultException nre) {
            System.out.println("ERROR: Job does not exist. " + nre.getMessage());
            em.remove(je);
            je = null;
        }
        return je;
    }

    public CategoryEntity lookupJobCategory(long jobCategoryID) {
        CategoryEntity ce = new CategoryEntity();
        try {
            Query q = em.createQuery("SELECT c FROM Category c WHERE c.categoryID = :jobCategoryID");
            q.setParameter("jobCategoryID", jobCategoryID);
            ce = (CategoryEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("ERROR: Job Category cannot be found. " + enfe.getMessage());
            em.remove(ce);
            ce = null;
        } catch (NoResultException nre) {
            System.out.println("ERROR: Job Category does not exist. " + nre.getMessage());
            em.remove(ce);
            ce = null;
        }
        return ce;
    }
}