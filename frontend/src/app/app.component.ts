import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  public isActive(button: MatButton) {
    return button._elementRef.nativeElement.classList.contains('active');
  }
}
