import {
  RouterLink
} from "./chunk-H7LWJ6FF.js";
import {
  ExamService
} from "./chunk-SPZR27HN.js";
import {
  MatCard,
  MatCardAvatar,
  MatCardContent,
  MatCardHeader,
  MatCardModule,
  MatCardTitle
} from "./chunk-C3J2YZRR.js";
import {
  MatProgressBar,
  MatProgressBarModule
} from "./chunk-K5Q6VE5O.js";
import {
  MatButton,
  MatButtonModule,
  MatIcon,
  MatIconModule
} from "./chunk-NKPCTZHW.js";
import {
  ChangeDetectionStrategy,
  Component,
  inject,
  setClassMetadata,
  signal,
  ɵsetClassDebugInfo,
  ɵɵadvance,
  ɵɵconditional,
  ɵɵconditionalCreate,
  ɵɵdefineComponent,
  ɵɵelement,
  ɵɵelementEnd,
  ɵɵelementStart,
  ɵɵrepeater,
  ɵɵrepeaterCreate,
  ɵɵtext,
  ɵɵtextInterpolate
} from "./chunk-F7VHYU5L.js";

// src/app/features/home/home.component.ts
var _forTrack0 = ($index, $item) => $item.title;
function HomeComponent_Conditional_16_Template(rf, ctx) {
  if (rf & 1) {
    \u0275\u0275element(0, "mat-progress-bar", 6);
  }
}
function HomeComponent_Conditional_17_Template(rf, ctx) {
  if (rf & 1) {
    \u0275\u0275elementStart(0, "mat-card", 7)(1, "mat-card-content")(2, "p", 13);
    \u0275\u0275text(3);
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(4, "p", 14);
    \u0275\u0275text(5, "examens enregistr\xE9s");
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(6, "ul", 15)(7, "li")(8, "span");
    \u0275\u0275text(9, "Planifi\xE9s");
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(10, "strong");
    \u0275\u0275text(11);
    \u0275\u0275elementEnd()();
    \u0275\u0275elementStart(12, "li")(13, "span");
    \u0275\u0275text(14, "En cours");
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(15, "strong");
    \u0275\u0275text(16);
    \u0275\u0275elementEnd()();
    \u0275\u0275elementStart(17, "li")(18, "span");
    \u0275\u0275text(19, "Termin\xE9s");
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(20, "strong");
    \u0275\u0275text(21);
    \u0275\u0275elementEnd()();
    \u0275\u0275elementStart(22, "li")(23, "span");
    \u0275\u0275text(24, "Annul\xE9s");
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(25, "strong");
    \u0275\u0275text(26);
    \u0275\u0275elementEnd()()()()();
  }
  if (rf & 2) {
    const stats_r1 = ctx;
    \u0275\u0275advance(3);
    \u0275\u0275textInterpolate(stats_r1.total);
    \u0275\u0275advance(8);
    \u0275\u0275textInterpolate(stats_r1.countByStatus.SCHEDULED);
    \u0275\u0275advance(5);
    \u0275\u0275textInterpolate(stats_r1.countByStatus.IN_PROGRESS);
    \u0275\u0275advance(5);
    \u0275\u0275textInterpolate(stats_r1.countByStatus.COMPLETED);
    \u0275\u0275advance(5);
    \u0275\u0275textInterpolate(stats_r1.countByStatus.CANCELLED);
  }
}
function HomeComponent_Conditional_18_Template(rf, ctx) {
  if (rf & 1) {
    \u0275\u0275elementStart(0, "mat-card", 8)(1, "mat-card-content");
    \u0275\u0275text(2, "Statistiques disponibles pour le moment.");
    \u0275\u0275elementEnd()();
  }
}
function HomeComponent_For_24_Template(rf, ctx) {
  if (rf & 1) {
    \u0275\u0275elementStart(0, "mat-card", 12)(1, "mat-card-header")(2, "mat-icon", 16);
    \u0275\u0275text(3);
    \u0275\u0275elementEnd();
    \u0275\u0275elementStart(4, "mat-card-title");
    \u0275\u0275text(5);
    \u0275\u0275elementEnd()();
    \u0275\u0275elementStart(6, "mat-card-content");
    \u0275\u0275text(7);
    \u0275\u0275elementEnd()();
  }
  if (rf & 2) {
    const service_r2 = ctx.$implicit;
    \u0275\u0275advance(3);
    \u0275\u0275textInterpolate(service_r2.icon);
    \u0275\u0275advance(2);
    \u0275\u0275textInterpolate(service_r2.title);
    \u0275\u0275advance(2);
    \u0275\u0275textInterpolate(service_r2.description);
  }
}
var HomeComponent = class _HomeComponent {
  examService = inject(ExamService);
  statistics = signal(null, ...ngDevMode ? [{ debugName: "statistics" }] : []);
  statistics1 = signal([], ...ngDevMode ? [{ debugName: "statistics1" }] : []);
  loading = signal(true, ...ngDevMode ? [{ debugName: "loading" }] : []);
  services = [
    {
      icon: "monitor_heart",
      title: "Imagerie par r\xE9sonance magn\xE9tique",
      description: "Deux IRM 1.5T pour les explorations neurologiques et ost\xE9o-articulaires."
    },
    {
      icon: "scanner",
      title: "Scanner",
      description: "Scanner multibarrettes basse dose, r\xE9sultats transmis au m\xE9decin prescripteur."
    },
    {
      icon: "radio_button_checked",
      title: "Radiologie et \xE9chographie",
      description: "Radiologie num\xE9rique, \xE9chographie et mammographie sur rendez-vous."
    }
  ];
  constructor() {
    this.examService.getChronostatistics().subscribe((data) => {
      this.statistics1.set(data);
      console.log(this.statistics1);
      this.loading.set(false);
    });
  }
  static \u0275fac = function HomeComponent_Factory(__ngFactoryType__) {
    return new (__ngFactoryType__ || _HomeComponent)();
  };
  static \u0275cmp = /* @__PURE__ */ \u0275\u0275defineComponent({ type: _HomeComponent, selectors: [["app-home"]], decls: 25, vars: 1, consts: [[1, "hero"], [1, "hero-text"], [1, "hero-actions"], ["mat-flat-button", "", "color", "primary", "routerLink", "/exams"], ["mat-stroked-button", "", "href", "tel:+33100000000"], [1, "hero-stats"], ["mode", "indeterminate", "aria-label", "Chargement des statistiques"], ["appearance", "outlined"], ["appearance", "outlined", "routerLink", "/statistic"], ["aria-labelledby", "services-title", 1, "services"], ["id", "services-title"], [1, "service-grid"], ["appearance", "outlined", 1, "service-card"], [1, "stat-value"], [1, "stat-label"], [1, "stat-breakdown"], ["mat-card-avatar", "", "aria-hidden", "true"]], template: function HomeComponent_Template(rf, ctx) {
    if (rf & 1) {
      \u0275\u0275elementStart(0, "section", 0)(1, "div", 1)(2, "h1");
      \u0275\u0275text(3, "Cabinet de radiologie");
      \u0275\u0275elementEnd();
      \u0275\u0275elementStart(4, "p");
      \u0275\u0275text(5, " Planification des examens d'imagerie, suivi des salles et des cr\xE9neaux, sans aucune donn\xE9e m\xE9dicale stock\xE9e. ");
      \u0275\u0275elementEnd();
      \u0275\u0275elementStart(6, "div", 2)(7, "a", 3)(8, "mat-icon");
      \u0275\u0275text(9, "event_available");
      \u0275\u0275elementEnd();
      \u0275\u0275text(10, " G\xE9rer les examens ");
      \u0275\u0275elementEnd();
      \u0275\u0275elementStart(11, "a", 4)(12, "mat-icon");
      \u0275\u0275text(13, "call");
      \u0275\u0275elementEnd();
      \u0275\u0275text(14, " Nous appeler ");
      \u0275\u0275elementEnd()()();
      \u0275\u0275elementStart(15, "div", 5);
      \u0275\u0275conditionalCreate(16, HomeComponent_Conditional_16_Template, 1, 0, "mat-progress-bar", 6)(17, HomeComponent_Conditional_17_Template, 27, 5, "mat-card", 7)(18, HomeComponent_Conditional_18_Template, 3, 0, "mat-card", 8);
      \u0275\u0275elementEnd()();
      \u0275\u0275elementStart(19, "section", 9)(20, "h2", 10);
      \u0275\u0275text(21, "Nos plateaux techniques");
      \u0275\u0275elementEnd();
      \u0275\u0275elementStart(22, "div", 11);
      \u0275\u0275repeaterCreate(23, HomeComponent_For_24_Template, 8, 3, "mat-card", 12, _forTrack0);
      \u0275\u0275elementEnd()();
    }
    if (rf & 2) {
      let tmp_0_0;
      \u0275\u0275advance(16);
      \u0275\u0275conditional(ctx.loading() ? 16 : (tmp_0_0 = ctx.statistics()) ? 17 : 18, tmp_0_0);
      \u0275\u0275advance(7);
      \u0275\u0275repeater(ctx.services);
    }
  }, dependencies: [RouterLink, MatCardModule, MatCard, MatCardAvatar, MatCardContent, MatCardHeader, MatCardTitle, MatButtonModule, MatButton, MatIconModule, MatIcon, MatProgressBarModule, MatProgressBar], styles: ["\n\n.hero[_ngcontent-%COMP%] {\n  display: grid;\n  grid-template-columns: minmax(0, 3fr) minmax(0, 2fr);\n  gap: 2rem;\n  align-items: center;\n  margin-bottom: 3rem;\n}\n.hero[_ngcontent-%COMP%]   h1[_ngcontent-%COMP%] {\n  font-size: clamp(1.8rem, 4vw, 2.8rem);\n  margin: 0 0 0.75rem;\n}\n.hero[_ngcontent-%COMP%]   p[_ngcontent-%COMP%] {\n  max-width: 52ch;\n  line-height: 1.6;\n}\n.hero-actions[_ngcontent-%COMP%] {\n  display: flex;\n  flex-wrap: wrap;\n  gap: 0.75rem;\n  margin-top: 1.5rem;\n}\n.stat-value[_ngcontent-%COMP%] {\n  font-size: 2.5rem;\n  font-weight: 600;\n  margin: 0;\n}\n.stat-label[_ngcontent-%COMP%] {\n  margin: 0 0 1rem;\n  opacity: 0.7;\n}\n.stat-breakdown[_ngcontent-%COMP%] {\n  list-style: none;\n  margin: 0;\n  padding: 0;\n  display: grid;\n  gap: 0.35rem;\n}\n.stat-breakdown[_ngcontent-%COMP%]   li[_ngcontent-%COMP%] {\n  display: flex;\n  justify-content: space-between;\n}\n.service-grid[_ngcontent-%COMP%] {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));\n  gap: 1.25rem;\n}\n.service-card[_ngcontent-%COMP%]   mat-card-content[_ngcontent-%COMP%] {\n  line-height: 1.5;\n}\n@media (max-width: 899px) {\n  .hero[_ngcontent-%COMP%] {\n    grid-template-columns: 1fr;\n  }\n}\n/*# sourceMappingURL=home.component.css.map */"], changeDetection: 0 });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(HomeComponent, [{
    type: Component,
    args: [{ selector: "app-home", imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule], changeDetection: ChangeDetectionStrategy.OnPush, template: `<section class="hero">
  <div class="hero-text">
    <h1>Cabinet de radiologie</h1>
    <p>
      Planification des examens d'imagerie, suivi des salles et des cr\xE9neaux, sans aucune donn\xE9e
      m\xE9dicale stock\xE9e.
    </p>
    <div class="hero-actions">
      <a mat-flat-button color="primary" routerLink="/exams">
        <mat-icon>event_available</mat-icon>
        G\xE9rer les examens
      </a>
      <a mat-stroked-button href="tel:+33100000000">
        <mat-icon>call</mat-icon>
        Nous appeler
      </a>
    </div>
  </div>
  <div class="hero-stats">
    @if (loading()) {
      <mat-progress-bar mode="indeterminate" aria-label="Chargement des statistiques" />
    } @else if (statistics(); as stats) {
      <mat-card appearance="outlined">
        <mat-card-content>
          <p class="stat-value">{{ stats.total }}</p>
          <p class="stat-label">examens enregistr\xE9s</p>
          <ul class="stat-breakdown">
            <li><span>Planifi\xE9s</span><strong>{{ stats.countByStatus.SCHEDULED }}</strong></li>
            <li><span>En cours</span><strong>{{ stats.countByStatus.IN_PROGRESS }}</strong></li>
            <li><span>Termin\xE9s</span><strong>{{ stats.countByStatus.COMPLETED }}</strong></li>
            <li><span>Annul\xE9s</span><strong>{{ stats.countByStatus.CANCELLED }}</strong></li>
          </ul>
        </mat-card-content>
      </mat-card>
    } @else {
      <mat-card appearance="outlined" routerLink="/statistic">
        <mat-card-content>Statistiques disponibles pour le moment.</mat-card-content>
      </mat-card>
    }
  </div>
</section>

<section class="services" aria-labelledby="services-title">
  <h2 id="services-title">Nos plateaux techniques</h2>
  <div class="service-grid">
    @for (service of services; track service.title) {
      <mat-card appearance="outlined" class="service-card">
        <mat-card-header>
          <mat-icon mat-card-avatar aria-hidden="true">{{ service.icon }}</mat-icon>
          <mat-card-title>{{ service.title }}</mat-card-title>
        </mat-card-header>
        <mat-card-content>{{ service.description }}</mat-card-content>
      </mat-card>
    }
  </div>
</section>



`, styles: ["/* src/app/features/home/home.component.scss */\n.hero {\n  display: grid;\n  grid-template-columns: minmax(0, 3fr) minmax(0, 2fr);\n  gap: 2rem;\n  align-items: center;\n  margin-bottom: 3rem;\n}\n.hero h1 {\n  font-size: clamp(1.8rem, 4vw, 2.8rem);\n  margin: 0 0 0.75rem;\n}\n.hero p {\n  max-width: 52ch;\n  line-height: 1.6;\n}\n.hero-actions {\n  display: flex;\n  flex-wrap: wrap;\n  gap: 0.75rem;\n  margin-top: 1.5rem;\n}\n.stat-value {\n  font-size: 2.5rem;\n  font-weight: 600;\n  margin: 0;\n}\n.stat-label {\n  margin: 0 0 1rem;\n  opacity: 0.7;\n}\n.stat-breakdown {\n  list-style: none;\n  margin: 0;\n  padding: 0;\n  display: grid;\n  gap: 0.35rem;\n}\n.stat-breakdown li {\n  display: flex;\n  justify-content: space-between;\n}\n.service-grid {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));\n  gap: 1.25rem;\n}\n.service-card mat-card-content {\n  line-height: 1.5;\n}\n@media (max-width: 899px) {\n  .hero {\n    grid-template-columns: 1fr;\n  }\n}\n/*# sourceMappingURL=home.component.css.map */\n"] }]
  }], () => [], null);
})();
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && \u0275setClassDebugInfo(HomeComponent, { className: "HomeComponent", filePath: "src/app/features/home/home.component.ts", lineNumber: 25 });
})();
export {
  HomeComponent
};
//# sourceMappingURL=chunk-BZXUP42F.js.map
