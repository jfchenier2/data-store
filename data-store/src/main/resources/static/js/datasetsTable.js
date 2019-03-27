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

	 // for updating the current row and status information in the table.
	 var intervalID = setInterval( function () {
		 
		 var isUploading = table.column(2).data().toArray().includes("UPLOADING");
		 var isNormalizing = table.column(2).data().toArray().includes("NORMALIZING");
		 var isCreating = table.column(2).data().toArray().includes("CREATED");
		 
		 // clears the interval only if it's not in the process of uploading a dataset
		 if(!isUploading && !isNormalizing && !isCreating) {
			 clearInterval(intervalID);
//			 alert('Interval Cleared');
		 }else{
			 table.ajax.reload(null, false);
		 }
		
	 }, 300); 
	 
});

