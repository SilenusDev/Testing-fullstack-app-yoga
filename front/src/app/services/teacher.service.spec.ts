import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP en attente
  });

  // Test 1 : Vérifier que le service est bien créé
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test 2 : Vérifier que la méthode all retourne une liste de professeurs
  it('should return a list of teachers', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: new Date(), updatedAt: new Date() }
    ];

    service.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpMock.expectOne(service['pathService']);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  // Test 3 : Vérifier que la méthode detail retourne les détails d'un professeur spécifique
  it('should return details of a specific teacher', () => {
    const mockTeacher: Teacher = { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() };
    const teacherId = '1';

    service.detail(teacherId).subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne(`${service['pathService']}/${teacherId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});


