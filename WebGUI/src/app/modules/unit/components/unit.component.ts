import {Component, OnInit} from '@angular/core';
import {Unit} from 'app/modules/unit/models';
import {UnitService} from 'app/modules/unit/services/unit.service';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html'
})
export class UnitComponent implements OnInit {

  units: Unit[] = [];

  constructor(private unitService: UnitService) { }

  private static sortById(units: Unit[]): Unit[] {
    return units.sort((u1, u2) => u1.id - u2.id);
  }

  ngOnInit(): void {
    this.unitService.getAll()
      .pipe(map(UnitComponent.sortById))
      .subscribe(units => this.units = units);
  }
}
