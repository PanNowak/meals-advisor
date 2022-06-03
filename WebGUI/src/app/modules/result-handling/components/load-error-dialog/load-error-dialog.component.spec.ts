import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { LoadErrorDialogComponent } from 'app/modules/result-handling/components/load-error-dialog/load-error-dialog.component';

describe('LoadErrorDialogComponent', () => {
  let component: LoadErrorDialogComponent;
  let fixture: ComponentFixture<LoadErrorDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadErrorDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadErrorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
