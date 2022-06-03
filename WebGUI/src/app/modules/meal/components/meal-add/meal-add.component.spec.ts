import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { MealAddComponent } from 'src/app/modules/meal/components/meal-add/meal-add.component';

describe('MealAddComponent', () => {
  let component: MealAddComponent;
  let fixture: ComponentFixture<MealAddComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ MealAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MealAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
