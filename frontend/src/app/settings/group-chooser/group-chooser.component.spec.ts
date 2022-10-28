import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupChooserComponent } from './group-chooser.component';

describe('GroupChooserComponent', () => {
  let component: GroupChooserComponent;
  let fixture: ComponentFixture<GroupChooserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupChooserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupChooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
