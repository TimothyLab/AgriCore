package agricore.projet.dto.compte.request;


public record TransfertRequestDTO(


        Integer sourceId,


        Integer destinationId,

        Integer montant
) {
}
