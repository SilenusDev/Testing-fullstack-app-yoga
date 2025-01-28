import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP en attente
  });

  // Test 1 : Vérifier que le service est bien créé
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test 2 : Vérifier que la méthode getById retourne les détails d'un utilisateur spécifique
  it('should return details of a specific user', () => {
    const mockUser: User = {
      id: 1,
      email: 'user1@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    const userId = '1';

    service.getById(userId).subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  // Test 3 : Vérifier que la méthode delete supprime un utilisateur
  it('should delete a user', () => {
    const userId = '1';

    service.delete(userId).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});

