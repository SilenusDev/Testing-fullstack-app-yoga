import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  // Test 1 : Vérifier que le service est bien créé
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test 2 : Vérifier que la méthode logIn met à jour correctement les informations de session et l'état de connexion
  it('should update session information and login status on logIn', () => {
    const user: SessionInformation = {
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    };

    service.logIn(user);

    expect(service.sessionInformation).toEqual(user);
    expect(service.isLogged).toBe(true);
  });

  // Test 3 : Vérifier que la méthode logOut réinitialise correctement les informations de session et l'état de connexion
  it('should reset session information and login status on logOut', () => {
    const user: SessionInformation = {
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    };

    service.logIn(user);
    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);
  });

  // Test 4 : Vérifier que la méthode $isLogged retourne un Observable de l'état de connexion
  it('should return an Observable of login status', () => {
    const loginStatus$ = service.$isLogged();

    loginStatus$.subscribe(status => {
      expect(status).toBe(service.isLogged);
    });

    service.logIn({
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    });

    loginStatus$.subscribe(status => {
      expect(status).toBe(true);
    });

    service.logOut();

    loginStatus$.subscribe(status => {
      expect(status).toBe(false);
    });
  });

  // Test 5 : Vérifier que les boutons Create et Detail apparaissent si l'utilisateur connecté est un admin
  it('should show Create and Detail buttons if the logged-in user is an admin', () => {
    const user: SessionInformation = {
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    };

    service.logIn(user);

    // Simuler l'affichage des boutons Create et Detail
    const showCreateButton = service.sessionInformation?.admin;
    const showDetailButton = service.sessionInformation?.admin;

    expect(showCreateButton).toBe(true);
    expect(showDetailButton).toBe(true);
  });

  // Test 6 : Vérifier que les boutons Create et Detail n'apparaissent pas si l'utilisateur connecté n'est pas un admin
  it('should not show Create and Detail buttons if the logged-in user is not an admin', () => {
    const user: SessionInformation = {
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    };

    service.logIn(user);

    // Simuler l'affichage des boutons Create et Detail
    const showCreateButton = service.sessionInformation?.admin;
    const showDetailButton = service.sessionInformation?.admin;

    expect(showCreateButton).toBe(false);
    expect(showDetailButton).toBe(false);
  });
});
