import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TextTabComponent } from './text-tab.component';

describe('TextTabComponent', () => {
  let component: TextTabComponent;
  let fixture: ComponentFixture<TextTabComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TextTabComponent]
    });
    fixture = TestBed.createComponent(TextTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
