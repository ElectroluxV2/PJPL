import {Directive, ElementRef, Input, OnDestroy, OnInit} from '@angular/core';

@Directive({
  selector: '[appScrollToCurrent]'
})
export class ScrollToCurrentDirective implements OnInit, OnDestroy {
  @Input() disableScrollToCurrent = false;
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
    const current = document.querySelector('[current]');

    if (!current) {
      return;
    }
    if (this.disableScrollToCurrent) {
      return;
    }

    current?.scrollIntoView({
      block: "center"
    });
  }
}
