import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
// import { TestBed } from '@angular/core/testing';
// import { SessionApiService } from './session-api.service';
// import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Session',
    description: 'A calming yoga session',
    date: new Date(),
    teacher_id: 123,  // Exemple de teacher_id
    users: [1, 2, 3],  // Liste des utilisateurs
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Assurez-vous que toutes les requêtes HTTP en attente sont terminées.
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual([mockSession]);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush([mockSession]);
  });

  it('should fetch session details by ID', () => {
    const sessionId = '1';
    service.detail(sessionId).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session by ID', () => {
    const sessionId = '1';
    service.delete(sessionId).subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a session', () => {
    service.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('should update a session by ID', () => {
    const sessionId = '1';
    service.update(sessionId, mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('should add a user to a session (participate)', () => {
    const sessionId = '1';
    const userId = '1';
    service.participate(sessionId, userId).subscribe((response) => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('should remove a user from a session (unParticipate)', () => {
    const sessionId = '1';
    const userId = '1';
    service.unParticipate(sessionId, userId).subscribe((response) => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});


// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
// import { TestBed } from '@angular/core/testing';
// import { SessionApiService } from './session-api.service';
// import { Session } from '../interfaces/session.interface';

// describe('SessionApiService', () => {
//   let service: SessionApiService;
//   let httpMock: HttpTestingController;

//   const mockSession: Session = {
//     id: 1,
//     name: 'Yoga Session',
//     description: 'A calming yoga session',
//     date: new Date(),
//     teacher_id: 123,  // Exemple de teacher_id
//     users: [1, 2, 3],  // Liste des utilisateurs
//     createdAt: new Date(),
//     updatedAt: new Date(),
//   };

//   beforeEach(() => {
//     TestBed.configureTestingModule({
//       imports: [HttpClientTestingModule],
//       providers: [SessionApiService]
//     });
//     service = TestBed.inject(SessionApiService);
//     httpMock = TestBed.inject(HttpTestingController);
//   });

//   afterEach(() => {
//     // Assurez-vous que toutes les requêtes HTTP en attente sont terminées.
//     httpMock.verify();
//   });

//   it('should be created', () => {
//     expect(service).toBeTruthy();
//   });

//   it('should fetch all sessions', () => {
//     service.all().subscribe((sessions) => {
//       expect(sessions).toEqual([mockSession]);
//     });

//     const req = httpMock.expectOne('api/session');
//     expect(req.request.method).toBe('GET');
//     req.flush([mockSession]);
//   });

//   it('should fetch session details by ID', () => {
//     const sessionId = '1';
//     service.detail(sessionId).subscribe((session) => {
//       expect(session).toEqual(mockSession);
//     });

//     const req = httpMock.expectOne(`api/session/${sessionId}`);
//     expect(req.request.method).toBe('GET');
//     req.flush(mockSession);
//   });

//   it('should delete a session by ID', () => {
//     const sessionId = '1';
//     service.delete(sessionId).subscribe((response) => {
//       expect(response).toBeTruthy();
//     });

//     const req = httpMock.expectOne(`api/session/${sessionId}`);
//     expect(req.request.method).toBe('DELETE');
//     req.flush({});
//   });

//   it('should create a session', () => {
//     service.create(mockSession).subscribe((session) => {
//       expect(session).toEqual(mockSession);
//     });

//     const req = httpMock.expectOne('api/session');
//     expect(req.request.method).toBe('POST');
//     expect(req.request.body).toEqual(mockSession);
//     req.flush(mockSession);
//   });

//   it('should update a session by ID', () => {
//     const sessionId = '1';
//     service.update(sessionId, mockSession).subscribe((session) => {
//       expect(session).toEqual(mockSession);
//     });

//     const req = httpMock.expectOne(`api/session/${sessionId}`);
//     expect(req.request.method).toBe('PUT');
//     expect(req.request.body).toEqual(mockSession);
//     req.flush(mockSession);
//   });

//   it('should add a user to a session (participate)', () => {
//     const sessionId = '1';
//     const userId = '1';
//     service.participate(sessionId, userId).subscribe((response) => {
//       expect(response).toBeUndefined();
//     });

//     const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
//     expect(req.request.method).toBe('POST');
//     req.flush(null);
//   });

//   it('should remove a user from a session (unParticipate)', () => {
//     const sessionId = '1';
//     const userId = '1';
//     service.unParticipate(sessionId, userId).subscribe((response) => {
//       expect(response).toBeUndefined();
//     });

//     const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
//     expect(req.request.method).toBe('DELETE');
//     req.flush(null);
//   });
// });

// describe('SessionApiService', () => {
//   let service: SessionApiService;
//   let httpMock: HttpTestingController;

//   beforeEach(() => {
//     // Configuration du module de test avec HttpClientTestingModule pour simuler les appels HTTP
//     TestBed.configureTestingModule({
//       imports: [HttpClientTestingModule],
//       providers: [SessionApiService]
//     });

//     // Injection du service et du HttpTestingController
//     service = TestBed.inject(SessionApiService);
//     httpMock = TestBed.inject(HttpTestingController);
//   });

//   afterEach(() => {
//     // Vérifie qu'il n'y a pas de requêtes HTTP en attente après chaque test
//     httpMock.verify();
//   });

//   it('should be created', () => {
//     // Vérifie que le service est créé correctement
//     expect(service).toBeTruthy();
//   });

//   it('should fetch all sessions', () => {
//     // Définition des données mockées pour la réponse
//     const mockSessions: Session[] = [
//       { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Description 1' },
//       { id: 2, name: 'Session 2', date: new Date(), teacher_id: 2, description: 'Description 2' }
//     ];

//     // Appel de la méthode all()
//     service.all().subscribe(sessions => {
//       // Vérifie que les sessions retournées correspondent aux données mockées
//       expect(sessions).toEqual(mockSessions);
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL
//     const req = httpMock.expectOne(`${service['pathService']}`);
//     expect(req.request.method).toBe('GET');

//     // Répond à la requête avec les données mockées
//     req.flush(mockSessions);
//   });

//   it('should fetch session details', () => {
//     // Définition des données mockées pour la réponse
//     const mockSession: Session = { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Description 1' };

//     // Appel de la méthode detail()
//     service.detail('1').subscribe(session => {
//       // Vérifie que la session retournée correspond aux données mockées
//       expect(session).toEqual(mockSession);
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL
//     const req = httpMock.expectOne(`${service['pathService']}/1`);
//     expect(req.request.method).toBe('GET');

//     // Répond à la requête avec les données mockées
//     req.flush(mockSession);
//   });

//   it('should delete a session', () => {
//     // Appel de la méthode delete()
//     service.delete('1').subscribe(response => {
//       // Vérifie que la réponse est vide (aucun contenu retourné)
//       expect(response).toBeNull();
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL et méthode
//     const req = httpMock.expectOne(`${service['pathService']}/1`);
//     expect(req.request.method).toBe('DELETE');

//     // Répond à la requête avec une réponse vide
//     req.flush(null);
//   });

//   it('should create a session', () => {
//     // Définition des données mockées pour la réponse
//     const mockSession: Session = { id: 1, name: 'New Session', date: new Date(), teacher_id: 1, description: 'New Description' };

//     // Appel de la méthode create()
//     service.create(mockSession).subscribe(session => {
//       // Vérifie que la session retournée correspond aux données mockées
//       expect(session).toEqual(mockSession);
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL et méthode
//     const req = httpMock.expectOne(`${service['pathService']}`);
//     expect(req.request.method).toBe('POST');
//     expect(req.request.body).toEqual(mockSession);

//     // Répond à la requête avec les données mockées
//     req.flush(mockSession);
//   });

//   it('should update a session', () => {
//     // Définition des données mockées pour la réponse
//     const mockSession: Session = { id: 1, name: 'Updated Session', date: new Date(), teacher_id: 1, description: 'Updated Description' };

//     // Appel de la méthode update()
//     service.update('1', mockSession).subscribe(session => {
//       // Vérifie que la session retournée correspond aux données mockées
//       expect(session).toEqual(mockSession);
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL et méthode
//     const req = httpMock.expectOne(`${service['pathService']}/1`);
//     expect(req.request.method).toBe('PUT');
//     expect(req.request.body).toEqual(mockSession);

//     // Répond à la requête avec les données mockées
//     req.flush(mockSession);
//   });

//   it('should participate in a session', () => {
//     // Appel de la méthode participate()
//     service.participate('1', 'user1').subscribe(response => {
//       // Vérifie que la réponse est vide (aucun contenu retourné)
//       expect(response).toBeUndefined();
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL et méthode
//     const req = httpMock.expectOne(`${service['pathService']}/1/participate/user1`);
//     expect(req.request.method).toBe('POST');

//     // Répond à la requête avec une réponse vide
//     req.flush(null);
//   });

//   it('should unparticipate in a session', () => {
//     // Appel de la méthode unParticipate()
//     service.unParticipate('1', 'user1').subscribe(response => {
//       // Vérifie que la réponse est vide (aucun contenu retourné)
//       expect(response).toBeUndefined();
//     });

//     // Vérifie que la requête HTTP a été effectuée avec la bonne URL et méthode
//     const req = httpMock.expectOne(`${service['pathService']}/1/participate/user1`);
//     expect(req.request.method).toBe('DELETE');

//     // Répond à la requête avec une réponse vide
//     req.flush(null);
//   });
// });
