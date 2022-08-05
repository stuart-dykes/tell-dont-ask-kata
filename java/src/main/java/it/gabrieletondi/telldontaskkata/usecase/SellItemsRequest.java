package it.gabrieletondi.telldontaskkata.usecase;

import java.util.List;

public record SellItemsRequest(List<SellItemRequest> requests) {

	public SellItemsRequest( final List<SellItemRequest> requests ) {
		this.requests = List.copyOf( requests );
	}

	public List<SellItemRequest> getRequests() {
		return requests;
	}
}
