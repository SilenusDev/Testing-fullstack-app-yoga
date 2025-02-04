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
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [AuthService]
      });
      service = TestBed.inject(AuthService);
      httpMock = TestBed.inject(HttpTestingController);
    });
  
    afterEach(() => {
      httpMock.verify();
    });
  
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  
    describe('register', () => {
      it('should send a POST request to register', () => {
        const registerRequest: RegisterRequest = {
          email: 'test@example.com',
          firstName: 'John',
          lastName: 'Doe',
          password: 'password'
        };
  
        service.register(registerRequest).subscribe();
  
        const req = httpMock.expectOne(`${service['pathService']}/register`);
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(registerRequest);
        req.flush(null);
      });
    });
  
    describe('login', () => {
      it('should send a POST request to login', () => {
        const loginRequest: LoginRequest = {
          email: 'test@example.com',
          password: 'password'
        };
  
        const sessionInformation: SessionInformation = {
          token: 'fake-jwt-token',
          type: 'Bearer',
          id: 1,
          username: 'testuser',
          firstName: 'John',
          lastName: 'Doe',
          admin: false
        };
  
        service.login(loginRequest).subscribe((response) => {
          expect(response).toEqual(sessionInformation);
        });
  
        const req = httpMock.expectOne(`${service['pathService']}/login`);
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(loginRequest);
        req.flush(sessionInformation);
      });
    });
  });