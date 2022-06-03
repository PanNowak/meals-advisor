import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SecondaryUnitAddDialogComponent } from 'app/modules/product/components/secondary-unit-add-dialog/secondary-unit-add-dialog.component';

describe('AdditionDialogComponent', () => {
  let component: SecondaryUnitAddDialogComponent;
  let fixture: ComponentFixture<SecondaryUnitAddDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SecondaryUnitAddDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecondaryUnitAddDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
