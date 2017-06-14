package cdccm.serviceApi;

import java.util.List;

import cdccm.pojo.AssignActivityPOJO;

public interface CareProviderService {

	

	
	public void childPerformance(AssignActivityPOJO updatePerformance) ;
	List<AssignActivityPOJO> displayChild(int id);

}
