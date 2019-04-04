
<body>
<script language="javascript">
    $(window).load(function() {
        var alertName = $("#alertName").val(); // retrive using the ID of the input
        alert(alertName);  // The prompt will alert "Sample Result"
    });
</script>
<input id="alertName" type="hidden" value="${alertName}"/>
    </body>
