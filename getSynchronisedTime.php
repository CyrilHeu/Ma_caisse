<?php

    $maintenant = new DateTime();
	$timenow = strtotime($maintenant->format('Y-m-d H:i:s')); 
    echo $timenow;
    echo "<br>".$maintenant->format('Y-m-d H:i:s'); 

?>