import {
  environment
} from "./chunk-K5Q6VE5O.js";
import {
  HttpClient,
  HttpParams
} from "./chunk-NKPCTZHW.js";
import {
  Injectable,
  inject,
  setClassMetadata,
  ɵɵdefineInjectable
} from "./chunk-F7VHYU5L.js";

// src/app/core/services/exam.service.ts
var ExamService = class _ExamService {
  http = inject(HttpClient);
  baseUrl = `${environment.apiBaseUrl}/exams`;
  apiUrl = "http://localhost:8080/api/v1/exams/calculChronostatisticsExamMonth";
  list(query = {}) {
    let params = new HttpParams();
    if (query.status) {
      params = params.set("status", query.status);
    }
    if (query.modality) {
      params = params.set("modality", query.modality);
    }
    params = params.set("page", String(query.page ?? 0)).set("size", String(query.size ?? 20));
    return this.http.get(this.baseUrl, { params });
  }
  // statistics(): Observable<ExamStatistics> {
  //   return this.http.get<ExamStatistics>(`${this.baseUrl}/statistics`);
  // }
  statistics() {
    return this.http.get(`${this.baseUrl}/calculChronostatisticsExamMonth`);
  }
  getById(id) {
    return this.http.get(`${this.baseUrl}/${id}`);
  }
  create(payload) {
    return this.http.post(this.baseUrl, payload);
  }
  update(id, payload) {
    return this.http.put(`${this.baseUrl}/${id}`, payload);
  }
  updateStatus(id, status) {
    return this.http.patch(`${this.baseUrl}/${id}/status`, { status });
  }
  delete(id) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
  getChronostatistics() {
    return this.http.get(`http://localhost:8080/api/v1/exams/calculChronostatisticsExamMonth`);
  }
  static \u0275fac = function ExamService_Factory(__ngFactoryType__) {
    return new (__ngFactoryType__ || _ExamService)();
  };
  static \u0275prov = /* @__PURE__ */ \u0275\u0275defineInjectable({ token: _ExamService, factory: _ExamService.\u0275fac, providedIn: "root" });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(ExamService, [{
    type: Injectable,
    args: [{ providedIn: "root" }]
  }], null, null);
})();

export {
  ExamService
};
//# sourceMappingURL=chunk-SPZR27HN.js.map
