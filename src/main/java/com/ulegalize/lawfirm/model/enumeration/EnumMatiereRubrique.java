package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumMatiereRubrique {
    DROIT_FAMILLE(1L, 1L),
    DROIT_PATRIMONIAL(2L, 1L),
    DROIT_JEUNESSE(3L, 1L),
    DROIT_ENFANT(4L, 1L),
    PROTECTION_PERSONNE(5L, 1L),
    ADMINISTRATION_PROVISOIRE(6L, 1L),
    PROTECTION_VIE_PRIVEE(7L, 1L),
    DROITS_REELS_IMMOBILIERS(8L, 2L),
    PRIVILEGES_HYPOTHEQUES(9L, 2L),
    EXPROPRIATION(10L, 2L),
    BAUX_LOYER_BAUX_COMMERCIAUX(11L, 2L),
    DROIT_RURAL(12L, 2L),
    RESPONSABILITE(13L, 3L),
    ASSURANCES(14L, 3L),
    REPARATION_DOMMAGE(15L, 3L),
    DROIT_SAISIES(16L, 5L),
    ARBITRAGE_NATIONAL_INTERNATIONAL(17L, 5L),
    REGLEMENT_COLLECTIF_DETTES(18L, 5L),
    DROIT_SOCIETES(19L, 6L),
    FAILLITES(20L, 6L),
    ASBL(21L, 6L),
    CONTRATS_COMMERCIAUX(22L, 7L),
    DROIT_CONCURRENCE(23L, 7L),
    DROIT_BANCAIRE_CREDIT(24L, 7L),
    TRANSPORTS_TERRESTRES(25L, 8L),
    TRANSPORTS_FLUVIAUX(26L, 8L),
    TRANSPORTS_AERIENS(27L, 8L),
    DROIT_MARITIME(28L, 8L),
    DROITS_AUTEUR(29L, 9L),
    BREVETS(30L, 9L),
    DROIT_TRAVAIL(31L, 10L),
    DROIT_SECU_SOCIALE(32L, 10L),
    IMPOTS_DIRECTS(33L, 11L),
    IMPOTS_INDIRECTS(34L, 11L),
    DROIT_PENAL_GENERAL(35L, 12L),
    DROIT_PENAL_AFFAIRES(36L, 12L),
    DROIT_CONSTITUTIONNEL(37L, 13L),
    ADMINISTRATIF(38L, 13L),
    DROIT_URBANISME_ENVIRONNEMENT(39L, 13L),
    DROIT_MARCHES_PUBLICS(40L, 13L),
    DROIT_AGENTS_ETAT(41L, 13L),
    DROIT_ETRANGERS(42L, 13L),
    DROIT_INTERNATIONAL_PRIVE(43L, 15L),
    DROIT_INTERNATIONAL_PUBLIC(44L, 15L),
    DROIT_CONCURENCE(45L, 16L),
    LIBRE_CIRCULATION_MARCHANDISES(46L, 16L),
    FONCTION_PUBLIQUE_EUROPEENNE(47L, 16L),
    DROIT_INFORMATIQUE(48L, 17L),
    DROIT_COMMUNICATIONS_ELECTRONIQUES(49L, 17L),
    MEDIATION_FAMILIALE(50L, 18L),
    MEDIATION_MATIERE_SOCIALE(51L, 18L),
    MEDIATION_CIVILE_COMMERCIALE(52L, 18L),
    MEDIATION_PENALE(53L, 18L),
    MEDIATION_DETTES(54L, 18L),
    DROIT_RESPONSABILITE_MEDICALE(55L, 19L),
    DROIT_HOSPITALIER(56L, 19L),
    DROIT_PHARMACEUTIQUE(57L, 19L);

    @Getter
    private final Long id;
    @Getter
    private final Long matiereId;

    EnumMatiereRubrique(Long id, Long matiereId) {
        this.id = id;
        this.matiereId = matiereId;
    }

    public static EnumMatiereRubrique fromId(Long id) {
        for (EnumMatiereRubrique enumMatiereRubrique : values()) {
            if (enumMatiereRubrique.getId().equals(id)) {
                return enumMatiereRubrique;
            }
        }
        return null;
    }

    public static EnumMatiereRubrique fromMatiereId(Integer matiereId) {
        for (EnumMatiereRubrique enumMatiereRubrique : values()) {
            if (enumMatiereRubrique.getMatiereId().equals(matiereId)) {
                return enumMatiereRubrique;
            }
        }
        return null;
    }
}
