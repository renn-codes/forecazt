package com.zombieturtle.forecazt;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

import static com.zombieturtle.forecazt.dataManager.dataUpdSiren3.updateData;

public class jobGenWeek implements Job {

    public void execute(JobExecutionContext context) {
        try {
            updateData();
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
