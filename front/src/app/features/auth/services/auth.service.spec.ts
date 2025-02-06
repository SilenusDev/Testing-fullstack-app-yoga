import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    // Configuration du module de test Angular avec HttpClientTestingModule pour simuler les requêtes HTTP
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],  // Importation du module de test HTTP
      providers: [AuthService]  // Fourniture du service à tester
    });

    // Injection du service AuthService et du HttpTestingController pour les tests
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérification qu'il n'y a pas de requêtes HTTP en attente après chaque test
    httpMock.verify();
  });

  it('should be created', () => {
    // Test pour vérifier que le service AuthService est créé avec succès
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register', () => {
      // Données fictives pour la requête d'inscription
      const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password'
      };

      // Appel de la méthode register du service
      service.register(registerRequest).subscribe();

      // Vérification qu'une requête POST a été envoyée à l'URL d'inscription
      const req = httpMock.expectOne(`${service['pathService']}/register`);
      expect(req.request.method).toBe('POST');  // Vérification de la méthode HTTP
      expect(req.request.body).toEqual(registerRequest);  // Vérification du corps de la requête

      // Simulation de la réponse HTTP
      req.flush(null);
    });
  });

  describe('login', () => {
    it('should send a POST request to login', () => {
      // Données fictives pour la requête de connexion
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password'
      };

      // Données fictives pour la réponse de connexion
      const sessionInformation: SessionInformation = {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      };

      // Appel de la méthode login du service et vérification de la réponse
      service.login(loginRequest).subscribe((response) => {
        expect(response).toEqual(sessionInformation);  // Vérification de la réponse
      });

      // Vérification qu'une requête POST a été envoyée à l'URL de connexion
      const req = httpMock.expectOne(`${service['pathService']}/login`);
      expect(req.request.method).toBe('POST');  // Vérification de la méthode HTTP
      expect(req.request.body).toEqual(loginRequest);  // Vérification du corps de la requête

      // Simulation de la réponse HTTP avec les informations de session
      req.flush(sessionInformation);
    });
  });
});
