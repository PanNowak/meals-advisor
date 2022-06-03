import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecondaryUnitTableComponent } from 'app/modules/product/components/secondary-unit-table/secondary-unit-table.component';

describe('SecondaryUnitTableComponent', () => {
  let component: SecondaryUnitTableComponent;
  let fixture: ComponentFixture<SecondaryUnitTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SecondaryUnitTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SecondaryUnitTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
