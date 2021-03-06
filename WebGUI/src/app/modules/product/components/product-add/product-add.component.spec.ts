import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ProductAddComponent } from 'src/app/modules/product/components/product-add/product-add.component';

describe('ProductAddComponent', () => {
  let component: ProductAddComponent;
  let fixture: ComponentFixture<ProductAddComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
