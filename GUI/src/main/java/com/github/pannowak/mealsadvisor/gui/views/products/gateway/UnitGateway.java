package com.github.pannowak.mealsadvisor.gui.views.products.gateway;

import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.api.units.service.UnitService;
import com.github.pannowak.mealsadvisor.gui.CrudGateway;
import com.github.pannowak.mealsadvisor.gui.Gateway;

@Gateway
public class UnitGateway extends CrudGateway<Unit, Unit> {

    UnitGateway(UnitService unitService) {
        super(unitService);
    }
}
