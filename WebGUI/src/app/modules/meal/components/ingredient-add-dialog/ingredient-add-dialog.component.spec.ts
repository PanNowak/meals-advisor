import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IngredientAddDialogComponent } from 'src/app/modules/meal/components/ingredient-add-dialog/ingredient-add-dialog.component';

describe('IngredientAddDialogComponent', () => {
  let component: IngredientAddDialogComponent;
  let fixture: ComponentFixture<IngredientAddDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientAddDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientAddDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
