$(document).ready( function () {
	var table = $('#datasetsTable').DataTable({
		"sAjaxSource": "all",
		"sAjaxDataProp": "",
		"order": [[ 0, "asc" ]],
		"aoColumns": [
		    { "mData": "datasetConfiguration.nameEn"},
		    { "mData": "createDateTime" },
			{ "mData": "datasetStatus" },
			{ "mData": "totalRecords" },
			{ "mData": "currentRow" },
			{ "defaultContent": "" },
			
		]
	 });
	 
	 $('#datasetsTable tbody').on('click', 'tr', function () {
		 var data = table.row( this ).data();
		 window.location='viewDataset?id=' + data.id;
	 });
	 
	 var intervalID = setInterval( function () {
		 
		 var totalContainsZero = table.column(3).data().toArray().includes(0);
		 
		 if(!totalContainsZero){
			 clearInterval(intervalID);
			 //alert('Interval Cleared');
		 }else{
			 table.ajax.reload(null, false);
		 }
		
	 }, 300); 
	 
});
