/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2011 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.contrib.minibus.PConfigGroup;
import org.matsim.contrib.minibus.hook.PModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.ArrayList;
import java.util.Random;


/**
 * Entry point, registers all necessary hooks
 *
 * @author aneumann
 */
public final class RunMinibus {

    private final Config config ;

    public RunMinibus(final String [] args ) {
        PConfigGroup nConf = new PConfigGroup();  // remove
        System.out.println(nConf);  // remove
        Config aConf = ConfigUtils.createConfig();
        aConf.addModule(nConf);
        // new ConfigWriter(aConf).writeFileV2("C:/Users/dgrishchuk/Desktop/minibus_test_config/config.xml");
        config = ConfigUtils.loadConfig("scenarios/spb/config.xml", new PConfigGroup());
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);

    }

    public final void run() {
        Scenario scenario = ScenarioUtils.loadScenario(config);

        Controler controler = new Controler(scenario);

        removePercentOfPopulation(scenario.getPopulation(), 90);

        PopulationWriter populationWriter = new PopulationWriter(scenario.getPopulation());
        //populationWriter.write("10PercentPopulation.xml");

        controler.addOverridingModule(new PModule()) ;

        controler.run();
    }

    private void removePercentOfPopulation(Population population, int percent) {

        System.out.println("Number of all plans: " + population.getPersons().size());

        for (Person person : new ArrayList<Person>(population.getPersons().values())) {
            Random random = new Random();
            if (random.nextDouble() * 100 < percent) {
                population.removePerson(person.getId());
            }
        }

        System.out.println("Number of plans for testing: " + population.getPersons().size());
    }

    public final Config getConfig() {
        return this.config ;
    }

    public static void main(final String[] args) {
        new RunMinibus( args ).run() ;
    }
}