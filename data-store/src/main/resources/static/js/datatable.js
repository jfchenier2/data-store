$(document).ready( function () {
	 var table = $('#programsTable').DataTable({
			"sAjaxSource": "/programs/programs",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "id"},
			    { "mData": "leadAgency.acronym" },
				{ "mData": "division" },
				{ "mData": "name" },
				{ "mData": "frequency" },
				{ "mData": "applyMethod" },
				{ "mData": "awardManagementSystem" }
			]
	 });
	 $('#programsTable tbody').on('click', 'tr', function () {
		 var data = table.row( this ).data();
		 window.open('viewProgram?id=' + data.id);
	 });
});
