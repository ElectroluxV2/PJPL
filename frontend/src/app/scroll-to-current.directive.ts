import {Directive, ElementRef, OnDestroy, OnInit} from '@angular/core';

@Directive({
  selector: '[appScrollToCurrent]'
})
export class ScrollToCurrentDirective implements OnInit, OnDestroy {
  private observer = new MutationObserver(() => {
    this.scrollToCurrent();
  });

  constructor(private readonly el: ElementRef) {}

  public ngOnInit(): void {
    this.observer.observe(this.el.nativeElement, {
      childList: true
    });
  }

  public ngOnDestroy(): void {
    this.observer.disconnect();
  }

  private scrollToCurrent() {
    const current = document.querySelector('.day[current]');
    current?.scrollIntoView({
      block: "center"
    });
  }
}
