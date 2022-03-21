package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.model.Etat;
import fr.uga.im2ag.l3.miage.db.model.velo;
import fr.uga.im2ag.l3.miage.db.model.*;
import fr.uga.im2ag.l3.miage.db.model.Statut;
import fr.uga.im2ag.l3.miage.db.repository.api.StationRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class StationRepositoryImpl  extends BaseRepositoryImpl implements StationRepository {


    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public StationRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public void save(Station entity) { entityManager.persist(entity);}

    public void delete(Station entity) { entityManager.remove(entity);}

    public Station findById(Long id) {
        return entityManager.find(Station.class, id);
    }

    public List<Station> getAll(){
        return entityManager.createNamedQuery("get-all-stations", Station.class ).getResultList();

    }
    public List<Station> getByStatut(Statut statut){
        return entityManager.createQuery("Select s from Station s where s.status=?1" , Station.class).setParameter(1,statut).getResultList();
    }

    //Affiche la liste des velos disponibles
    public List<velo> getVelosByStation(Long id){
        var station = findById(id);

        return entityManager.createQuery("Select v from Station s join s.Contient b Join b.Propose v where b.estPresent=?1 and v.etat=?2 and b.etat=?3" , velo.class)
                .setParameter(1,true)
                .setParameter(2,Etat.OK).setParameter(3,Etat.OK).getResultList();
    }

    public int getNombreVeloParStation(Station station){
        int somme = 0 ;
        for( int i =0 ; i < station.getContient().size() ; i++){
                if( station.getContient().get(i).isEstPresent()){
                    somme++;
                }
        }
        return somme ;
    }

    public int getNombreVeloEndommageByStation( Station station){
        int somme = 0 ;
        for( int i =0 ; i < station.getContient().size() ; i++){
            if(station.getContient().get(i).getPropose()!=null) {
                if (!(station.getContient().get(i).getPropose().getEtat() == Etat.HS)) {
                    somme++;
                }
            }
        }
        return somme;
    }

    public int getNombreVeloOKByStation( Station station){
        return  getNombreVeloParStation( station ) - getNombreVeloEndommageByStation(  station) ;
    }



}
