
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" >
<head><link rel="shortcut icon" href="/gringlobal/images/sysblank.ico" /><link id="ctl00_lnkTheme" rel="stylesheet" media="screen" href="/gringlobal/styles/default.aspx?theme=theme1" /><link href="/gringlobal/styles/print.css" rel="stylesheet" type="text/css" media="print" /><title>
	Descriptors - GRIN-Global Web v 1.9.7.1
</title>
    <script type="text/javascript" src='/gringlobal/scripts/jquery-1.3.2.min.js'></script>
    <script type="text/javascript" src='/gringlobal/scripts/jquery.cookie.js'></script>
    <script type='text/javascript'>
        $(document).ready(function() {
            $('._blurme').blur();
            $('.btn').each(function() {
                var htmlStr = $(this).html();
                if (htmlStr.indexOf("<i>") == -1) {
                    $(this).append('<i>').append('<span>').wrapInner(document.createElement("SPAN")).prepend('<i>');
                }
            });
        });
    </script>
    
    
    <script type='text/javascript'>
        $(document).ready(function() {
            $('#choose-crop').click(function() {
                $('#panel-crop').toggle('fast');
            });
            $('#choose-descriptor').click(function() {
                $('#panel-descriptor').toggle('fast');
            });
            $('#select-descriptor-value').click(function() {
                $('#panel-select').toggle('fast');
            });
            $('#add-passport').click(function() {
                $('#divPassport').toggle('fast');
            });

            $('#show-results').click(function() {
                $('#panel-results').toggle('fast');
            });

            $('#panel-crop').show();
        });
        
        function actionItemClicked(kind) {
            var pks = pivotView.rememberPrimaryKeys('checked');

            if (pks.length == 0) {
                alert("You must select at least one accession from the list.");
                $("#divActions").hide("fast");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>

    <form name="aspnetForm" method="post" action="descriptors.aspx" id="aspnetForm">
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="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" />


<script src="scripts/jquery-ui-1.7.2.custom.min.js" type="text/javascript"></script>
<script src="scripts/jquery.autocomplete.js" type="text/javascript"></script>
<script src="scripts/pivoter.js" type="text/javascript"></script>
<script src="/gringlobal/WebResource.axd?d=ms45o0MZhqWDnvLtjW9bOm4UKq99DEPzSgRHTzLLvev1gFugaVyiccTrG2n0XmeaQ_pDpeuEyIifR4qpAz6Y3uD6ssk1&amp;t=635588438811475957" type="text/javascript"></script>
<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="10C3E913" />
<input type="hidden" name="__VIEWSTATEENCRYPTED" id="__VIEWSTATEENCRYPTED" value="" />
<input type="hidden" name="__EVENTVALIDATION" id="__EVENTVALIDATION" value="o3Ha6rp03YhL+QmzPTqjoaxAf52ba53fLGlgwJ7X6Jg4RVQh6J/ORD+hqO8CEgTwDxwpi1IJNbPli7im+B3hCChBsg4wy/XJTEppTq2N5ljhC43vJRHpMdM1wCVCDRE4/GIReAowquAFdkNtb/Ud1ptyUbQX1QcueDYsE+cOW2OgPsTIJadoUkN1BDLFpOWjbqn9P1mlj/vEfjVrSvKsrD8ynQ4StjF9+nXDoM1wizHbIcDVVa0np0vIlg9ZhfVxdyxHbn9bQ+3lwjqf0rZfNsUEi9+HTQL33adekxOdUceEZDgCHTY6huqqHNb3cdow22IdA4oDNsc5uUZUTa1Fj0Tcq78k+vADbsEKiXQnCvIB/G/xt3wIupAoY9x/gLkOHTRfmVrd32puVXC3BeFr3WjW/qR05nHrCvmeiw99n21Eo4B13QtzK0BxPPSCAwgEh0PgZZMTmPT/73gV97wEvEYFuVUgB63y0LrKVIutUFL+fZiO8GCfoMpAsga+se3wKuflmZ252FVlTz2KXHrMm3wkXVI5gfw3yIgbfkURq1f4roJxsafI3dDzVXReuXQlDQLxvNe3zy1jyowaOZGZ84hvSk9AKFXzNnpNmwEcUU+4JDlN1F6MwbuVA1LTgJNPBdnpvCY7+YnXGkat2JI3tK/yGkYlw+2tbetmOldgkwuQp9DIK0pLAwmLZQPcvruTEzJIzlwFQe1LfDy9MxN9Gg2x+DZ/ZhCUsSZepOFSD0xZbHridjIV1isatXVyRLmAYJynQ1/DqSc4r/+klhANq7dGwuBfJA6E9ece24o4eVdnsQ46fwbqBRIPUAq1ncgZmUItms8vfj4gj+VLaCdAMJbDkmXKogVrh89BcDgspXfhDCCzVFIZDsJEtgj4AX2cXhtDqprz+ARBtsrRmNN0+N3hnJzHoLA6FaouyogL/nU4d9UfXsnqqLiSOllfppQXLohocNJTxXqP46F3RKFV7UgmoUf+EAYAxj61Gpdb+NqPFVwCFM4jJMdNCNUE/qeMgNGpE06+uvMo/eHw6AmPSHkoJ+akNDhu6lmVmZTwHe+qSttC9rjdzJMBa1CSb9rGVKxo012nMTIY2wKYk5KJ+DSJdnjGgRH8nSX7uMAiT+vq9+R5h2+TkJ+agQ6QMIPFJXy9kdws/3obBdFTtr19XQQq11MM2QT9FXKyeWoDeHnAazaEILEIL1hrhsB6yVrGNn7565ttghdsoL7lFx2XEF+53zgIuAUCeqit7a32dylafk40I5yuZ7TwQ3VhHS/KCIiwhlLtZ/1Komc8gSY+KICn8TwOZi17fNHH4qVH4VEGMJEYl0HYIOT805+qff1E89E8NGTvvcBDfQwWIHels5fp59zxUVr5tsqv3JJEp1gA3HTgtDjjuOduGe6Tpzhtd3g7E1lAPyHiEF7EOJvhJm+nGzlNyKQKbzyiVlr0+HnHzryuvfnQ/gL6/yVJHR/E0nz6a+w1hdGzfI4Mc/VyqmYyG7TC2R2osV2RWNoYZ3HVLy8puBoOdIJhtWdsrHo7cfPRtjhx3ujJ7ipDvSNw20BVzLhdoO8OFyv5YHZVScu68u5jIk9oWqCyYlolXvA3/gTRmSG1idsMGLgbrvcD4zJRK4MYg0Zjt41IpfxrZUBzQSQ9cR1LUoF4bcTWA7dd/lnKUZTBGyijfbB+MdQdMSsPYJYW9dFmSC1EEdmOMwX1vsS8GPLTjhaboHXejMTyS9yf8ouYfu0pPkq4Lj5N7/ftfhaXMJGEJnTALJ+1QnxXU6hQN+r4EFiJFHvx5Q5nksNIILKUVg9JggsyAa3Ewb+uO6plv0zCZJ0I+87iunXP+Rt/" />
    <div id='outer-wrapper'>
        <div id='container'>
                <div id='header-wrapper' class="menu">
                <div id='header-login'><b><a id="ctl00_loginStatus" href="javascript:__doPostBack('ctl00$loginStatus$ctl02','')">Login</a>  </b>
                                </div> 
                <div id="header-register">
                    <div id="ctl00_pnlRegister">
	for returning member. Don't have an online profile? <b><a href='/gringlobal/useracct.aspx'>Register Now</a></b>
</div> 
                </div>
                 <div id='header-cart'>
<div class="cart"><table><tr><td>
    <a id="ctl00_ggCart_lnkViewCart" title="Build an order to request accessions" href="/gringlobal/cartview.aspx"><img title="Build an order to request accessions" src="/gringlobal/images/btn_viewcart.gif" alt="View Cart" border="0" /></a></td> <td>
    <span id="ctl00_ggCart_lblContents">No items in cart</span></td></tr></table> 
</div></div>
                <div id='header-contact'><a href="javascript: Contact Us" onclick="javascript:window.open('/gringlobal/contact.aspx','','scrollbars=yes,titlebar=no,width=570,height=380')"><b><u>Contact Us</u></b></a></div>
                <div id='header-sitetitle'>U.S. National Plant Germplasm System</div>
                
                <div id='header-navwrapper'>
                    
                    <a href="#ctl00_mnuMain_SkipLink"><img alt="Skip Navigation Links" src="/gringlobal/WebResource.axd?d=8IR10fEt8AX_HxFW4SyJhLEVao5EjxFaY5gJFmiVVOO0fFWnWB2Xc9mV3OOvK9dx7G1c1QxugugMmwSR1uhqJrXwijc1&amp;t=635588438811475957" width="0" height="0" border="0" /></a><table id="ctl00_mnuMain" class="nav" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn0"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/search.aspx?">Accessions</a></td><td width="0"><img src="/gringlobal/WebResource.axd?d=dvg7HY8rWMms-Urk0PygKO9TJXUMnybrm617lYoy52_UMntLcu_3ZPrLXAG2Odws-UxWQVSpcR57-LbseVU36IpDWsA1&amp;t=635588438811475957" alt="Expand Accessions" valign="middle" /></td>
			</tr>
		</table></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn1"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/descriptors.aspx?">Descriptors</a></td><td width="0"><img src="/gringlobal/WebResource.axd?d=dvg7HY8rWMms-Urk0PygKO9TJXUMnybrm617lYoy52_UMntLcu_3ZPrLXAG2Odws-UxWQVSpcR57-LbseVU36IpDWsA1&amp;t=635588438811475957" alt="Expand Descriptors" valign="middle" /></td>
			</tr>
		</table></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn2"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/taxon/taxonomyquery.aspx">Taxonomy</a></td><td width="0"><img src="/gringlobal/WebResource.axd?d=dvg7HY8rWMms-Urk0PygKO9TJXUMnybrm617lYoy52_UMntLcu_3ZPrLXAG2Odws-UxWQVSpcR57-LbseVU36IpDWsA1&amp;t=635588438811475957" alt="Expand Taxonomy" valign="middle" /></td>
			</tr>
		</table></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Build an order to request accessions" id="ctl00_mnuMainn3"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/cartview.aspx">View Cart</a></td>
			</tr>
		</table></td><td width="3"></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Queries and Reports" id="ctl00_mnuMainn4"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/query/query.aspx?">Reports</a></td>
			</tr>
		</table></td><td width="3"></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Manage My Profile" id="ctl00_mnuMainn5"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/userinfor.aspx?action=menu">My Profile</a></td><td width="0"><img src="/gringlobal/WebResource.axd?d=dvg7HY8rWMms-Urk0PygKO9TJXUMnybrm617lYoy52_UMntLcu_3ZPrLXAG2Odws-UxWQVSpcR57-LbseVU36IpDWsA1&amp;t=635588438811475957" alt="Expand My Profile" valign="middle" /></td>
			</tr>
		</table></td><td onmouseover="Menu_HoverStatic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn6"><table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td nowrap="nowrap"><a href="/gringlobal/popuphelp.aspx?tag=Default.htm?" target="_blank">Help</a></td><td width="0"><img src="/gringlobal/WebResource.axd?d=dvg7HY8rWMms-Urk0PygKO9TJXUMnybrm617lYoy52_UMntLcu_3ZPrLXAG2Odws-UxWQVSpcR57-LbseVU36IpDWsA1&amp;t=635588438811475957" alt="Expand Help" valign="middle" /></td>
			</tr>
		</table></td>
	</tr>
</table><div id="ctl00_mnuMainn0Items" class="menupopup" style="display:none;">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Search GRIN-Global" id="ctl00_mnuMainn7">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/search.aspx">Search - General</a></td>
				</tr>
			</table></td>
		</tr>
	</table><div id="ctl00_mnuMainn0ItemsUp" onmouseover="PopOut_Up(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=BxjUjQfqlUmhkxqPkiJoZffgwg46YOAYij1EhCJUEEdLl-_pekHiN-QJcj4_2e7O00lQysl6xrt6JlJVKcLfdP4MyqA1&amp;t=635588438811475957" alt="Scroll up" />
	</div><div id="ctl00_mnuMainn0ItemsDn" onmouseover="PopOut_Down(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=M1m1iy2kIJ5QsAFMyA2YYSUvirvvB90tqNWxBGkhkKNXeEKRTIP9LPMZ3j5cwpF7mFHgddaOub5uQCMlAyMyX0ndYDo1&amp;t=635588438811475957" alt="Scroll down" />
	</div>
</div><div id="ctl00_mnuMainn1Items" class="menupopup" style="display:none;">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Search GRIN-Global via Descriptors" id="ctl00_mnuMainn8">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/descriptors.aspx">Search - Descriptors</a></td>
				</tr>
			</table></td>
		</tr>
	</table><div id="ctl00_mnuMainn1ItemsUp" onmouseover="PopOut_Up(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=BxjUjQfqlUmhkxqPkiJoZffgwg46YOAYij1EhCJUEEdLl-_pekHiN-QJcj4_2e7O00lQysl6xrt6JlJVKcLfdP4MyqA1&amp;t=635588438811475957" alt="Scroll up" />
	</div><div id="ctl00_mnuMainn1ItemsDn" onmouseover="PopOut_Down(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=M1m1iy2kIJ5QsAFMyA2YYSUvirvvB90tqNWxBGkhkKNXeEKRTIP9LPMZ3j5cwpF7mFHgddaOub5uQCMlAyMyX0ndYDo1&amp;t=635588438811475957" alt="Scroll down" />
	</div>
</div><div id="ctl00_mnuMainn2Items" class="menupopup" style="display:none;">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="GRIN Taxonomy for Plants Simple Query" id="ctl00_mnuMainn9">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxon/taxonomysimple.aspx">Simple Query Species Data</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Browse Taxonomy" id="ctl00_mnuMainn10">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxonomybrowse.aspx?">Browse Taxonomy</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="GRIN Taxonomy for Plants General Query" id="ctl00_mnuMainn11">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxon/taxonomysearch.aspx">Advanced Query of Species Data</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="GRIN Taxonomy for Plants Family/Genus Query" id="ctl00_mnuMainn12">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxon/famgensearch.aspx">Families and Genera Query</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="GRIN Taxonomy for Plants Economic Plants Query" id="ctl00_mnuMainn13">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxon/taxonomysearcheco.aspx">World Economic Plants in GRIN</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn14">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/taxon/abouttaxonomy.aspx">About GRIN Taxonomy for Plants</a></td>
				</tr>
			</table></td>
		</tr>
	</table><div id="ctl00_mnuMainn2ItemsUp" onmouseover="PopOut_Up(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=BxjUjQfqlUmhkxqPkiJoZffgwg46YOAYij1EhCJUEEdLl-_pekHiN-QJcj4_2e7O00lQysl6xrt6JlJVKcLfdP4MyqA1&amp;t=635588438811475957" alt="Scroll up" />
	</div><div id="ctl00_mnuMainn2ItemsDn" onmouseover="PopOut_Down(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=M1m1iy2kIJ5QsAFMyA2YYSUvirvvB90tqNWxBGkhkKNXeEKRTIP9LPMZ3j5cwpF7mFHgddaOub5uQCMlAyMyX0ndYDo1&amp;t=635588438811475957" alt="Scroll down" />
	</div>
</div><div id="ctl00_mnuMainn5Items" class="menupopup" style="display:none;">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Order History" id="ctl00_mnuMainn15">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/orderhistory.aspx">My Order History</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Manage My Preference" id="ctl00_mnuMainn16">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/userpref.aspx">My Preference</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Manage My Address Book" id="ctl00_mnuMainn17">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/useraddress.aspx">My Address Book</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" title="Display/Manage My Favoirtes" id="ctl00_mnuMainn18">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/userfavorite.aspx">My Favorites</a></td>
				</tr>
			</table></td>
		</tr>
	</table><div id="ctl00_mnuMainn5ItemsUp" onmouseover="PopOut_Up(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=BxjUjQfqlUmhkxqPkiJoZffgwg46YOAYij1EhCJUEEdLl-_pekHiN-QJcj4_2e7O00lQysl6xrt6JlJVKcLfdP4MyqA1&amp;t=635588438811475957" alt="Scroll up" />
	</div><div id="ctl00_mnuMainn5ItemsDn" onmouseover="PopOut_Down(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=M1m1iy2kIJ5QsAFMyA2YYSUvirvvB90tqNWxBGkhkKNXeEKRTIP9LPMZ3j5cwpF7mFHgddaOub5uQCMlAyMyX0ndYDo1&amp;t=635588438811475957" alt="Scroll down" />
	</div>
</div><div id="ctl00_mnuMainn6Items" class="menupopup" style="display:none;">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn19">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/popuphelp.aspx?tag=Default.htm" target="_blank">GRIN-Global Help</a></td>
				</tr>
			</table></td>
		</tr><tr onmouseover="Menu_HoverDynamic(this)" onmouseout="Menu_Unhover(this)" onkeyup="Menu_Key(event)" id="ctl00_mnuMainn20">
			<td><table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td nowrap="nowrap" width="100%"><a href="/gringlobal/popuphelp.aspx?tag=about" target="_blank">About GRIN-Global</a></td>
				</tr>
			</table></td>
		</tr>
	</table><div id="ctl00_mnuMainn6ItemsUp" onmouseover="PopOut_Up(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=BxjUjQfqlUmhkxqPkiJoZffgwg46YOAYij1EhCJUEEdLl-_pekHiN-QJcj4_2e7O00lQysl6xrt6JlJVKcLfdP4MyqA1&amp;t=635588438811475957" alt="Scroll up" />
	</div><div id="ctl00_mnuMainn6ItemsDn" onmouseover="PopOut_Down(this)" onmouseout="PopOut_Stop(this)" align="center" style="display:none;">
		<img src="/gringlobal/WebResource.axd?d=M1m1iy2kIJ5QsAFMyA2YYSUvirvvB90tqNWxBGkhkKNXeEKRTIP9LPMZ3j5cwpF7mFHgddaOub5uQCMlAyMyX0ndYDo1&amp;t=635588438811475957" alt="Scroll down" />
	</div>
</div><a id="ctl00_mnuMain_SkipLink"></a>
                    <span id="ctl00_smp" class="breadcrumb"><a href="#ctl00_smp_SkipLink"><img alt="Skip Navigation Links" height="0" width="0" src="/gringlobal/WebResource.axd?d=8IR10fEt8AX_HxFW4SyJhLEVao5EjxFaY5gJFmiVVOO0fFWnWB2Xc9mV3OOvK9dx7G1c1QxugugMmwSR1uhqJrXwijc1&amp;t=635588438811475957" border="0" /></a><span><a title="GRIN-Global" href="/gringlobal/default.aspx">Home</a></span><span> &gt; </span><span><a href="/gringlobal/descriptors.aspx?">Descriptors</a></span><span> &gt; </span><span>Descriptors</span><a id="ctl00_smp_SkipLink"></a></span>
                    <br />
                    <br />
                </div><!-- end header_navwrapper -->
                <div id='header-language' class="hide">
                    <span id="ctl00_lblChoose">Choose language</span>&nbsp;<select name="ctl00$ddlLanguage" id="ctl00_ddlLanguage" class="_blurme">
	<option value="7">Česk&#253;</option>
	<option value="9">ENG</option>
	<option selected="selected" value="1">English</option>
	<option value="2">Espa&#241;ol</option>
	<option value="3">Fran&#231;ais</option>
	<option value="6">Portugu&#234;s</option>
	<option value="8">System</option>
	<option value="5">Русский</option>
	<option value="4">العربية</option>

</select></div>
                </div><!-- end header-wrapper -->

                <div id='content-wrapper'>
                    <div id='content'>
                     
                     
                     
                     
                    
                       
    <table><tr><td>
        <h3 >Choose Crop:   
           &nbsp;&nbsp; <span id="ctl00_cphBody_lblCrop" title="Go to crop page"></span></h3> </td><td>&nbsp;<span id="ctl00_cphBody_lblCropLink"></span></td></tr></table>
                <div id='panel-crop2' class=''>
                    <select name="ctl00$cphBody$ddlCrops" id="ctl00_cphBody_ddlCrops" class="ddl_crops">
	<option selected="selected" value="-1"> -- Select One -- </option>
	<option value="295">ACEROLA</option>
	<option value="237">ACTAEA</option>
	<option value="77">AEGILOPS</option>
	<option value="68">ALFALFA</option>
	<option value="90">ALLIUM</option>
	<option value="232">ALLIUM-GARLIC</option>
	<option value="71">ALLIUM-WILD</option>
	<option value="266">ALMOND</option>
	<option value="159">AMARANTH</option>
	<option value="119">APIUM</option>
	<option value="115">APPLE</option>
	<option value="265">APRICOT</option>
	<option value="298">ARRACACHA</option>
	<option value="296">ARTICHOKE</option>
	<option value="297">ASPARAGUS</option>
	<option value="130">ASTRAGALUS</option>
	<option value="300">AVOCADO</option>
	<option value="184">BAMBARA-GROUNDNUT</option>
	<option value="251">BAMBOO</option>
	<option value="1">BARLEY</option>
	<option value="127">BARLEY-GENETICS</option>
	<option value="253">BASSIA</option>
	<option value="285">BLACKBERRY</option>
	<option value="286">BLACK-RASPBERRY</option>
	<option value="138">BRASSICA</option>
	<option value="306">BRASSICACEAE-NC7MISC</option>
	<option value="217">BREADFRUIT</option>
	<option value="172">BUTTERNUT</option>
	<option value="207">CABBAGE</option>
	<option value="256">CACAO</option>
	<option value="249">CALENDULA</option>
	<option value="150">CANTALOUPE</option>
	<option value="178">CARAMBOLA</option>
	<option value="233">CARYA</option>
	<option value="301">CASSAVA</option>
	<option value="97">CASTOR</option>
	<option value="243">CELOSIA-GROUP</option>
	<option value="239">CHAMAELIRIUM</option>
	<option value="400">CHAYOTE</option>
	<option value="158">CHERRY</option>
	<option value="275">CHERRY-SWEET</option>
	<option value="284">CHESTNUT</option>
	<option value="54">CHICKPEA</option>
	<option value="406">CHINA TEA</option>
	<option value="194">CICHORIUM</option>
	<option value="402">CINNAMON</option>
	<option value="112">CITRUS</option>
	<option value="294">CLOVER-ANNUALS</option>
	<option value="293">CLOVER-PERENNIALS</option>
	<option value="299">COFFEE</option>
	<option value="241">CORIANDER</option>
	<option value="153">COTTON</option>
	<option value="123">CUCUMIS</option>
	<option value="114">CUCURBITA</option>
	<option value="220">CUCURBITA-MOSCHATA</option>
	<option value="104">CUPHEA</option>
	<option value="303">DATE</option>
	<option value="70">DAUCUS</option>
	<option value="279">DIFFERENTIAL-SETS</option>
	<option value="221">ECHINACEA</option>
	<option value="131">EGGPLANT</option>
	<option value="103">FABABEAN</option>
	<option value="192">FIG</option>
	<option value="87">FLAX</option>
	<option value="312">GIANT-TARO</option>
	<option value="405">GINGER</option>
	<option value="124">GLYCINE-PERENNIAL</option>
	<option value="174">GRAPE</option>
	<option value="273">GRAPE-DAVIS</option>
	<option value="110">GRASS-COOLSEASON</option>
	<option value="244">GRASSES-MINOR-NC7</option>
	<option value="102">GRASS-WARMSEASON</option>
	<option value="96">GUAR</option>
	<option value="156">GUAVA</option>
	<option value="240">HARDY-KIWIFRUIT</option>
	<option value="105">HAZELNUT</option>
	<option value="155">HIBISCUS</option>
	<option value="180">HOPS</option>
	<option value="236">HYPERICUM</option>
	<option value="183">INTERGENERIC</option>
	<option value="276">KIWIFRUIT</option>
	<option value="250">LAMIACEAE-NC7</option>
	<option value="280">LANSIUM</option>
	<option value="118">LATHYRUS</option>
	<option value="107">LENTIL</option>
	<option value="179">LESQUERELLA</option>
	<option value="80">LETTUCE</option>
	<option value="212">LIMNANTHES</option>
	<option value="176">LITCHI</option>
	<option value="223">LONGAN</option>
	<option value="302">LOQUAT</option>
	<option value="142">LUPIN</option>
	<option value="283">MACADAMIA</option>
	<option value="89">MAIZE</option>
	<option value="137">MAIZE-GENSTOCKS</option>
	<option value="125">MAIZE-LAMP</option>
	<option value="248">MALVACEAE-NC7</option>
	<option value="292">MAMEY-SAPOTE</option>
	<option value="309">MANGO</option>
	<option value="145">MEDIC</option>
	<option value="259">MEDICINALS-NC7</option>
	<option value="224">MILLET-BARNYARD</option>
	<option value="225">MILLET-FOXTAIL</option>
	<option value="226">MILLET-PROSO</option>
	<option value="117">MINT</option>
	<option value="308">MONARDA</option>
	<option value="182">MOUNTAIN-MINT</option>
	<option value="272">MULBERRY</option>
	<option value="215">MUSA</option>
	<option value="6">NC7-CUCUMIS</option>
	<option value="7">NC7-CUCURBITA</option>
	<option value="8">NC7-DAUCUS</option>
	<option value="148">NC7-FORAGE-LEGUMES</option>
	<option value="140">NC7-HELIANTHUS</option>
	<option value="139">NE9-GRASSES</option>
	<option value="66">OAT</option>
	<option value="235">OCIMUM</option>
	<option value="95">OKRA</option>
	<option value="254">OLIVE</option>
	<option value="252">OPUNTIA</option>
	<option value="310">ORIGANUM</option>
	<option value="264">PALMER-CALAMAGROSTIS</option>
	<option value="262">PALMER-MISC</option>
	<option value="263">PALMER-WETLAND</option>
	<option value="126">PAPAYA</option>
	<option value="227">PARTHENIUM</option>
	<option value="234">PASTINACA</option>
	<option value="177">PEA</option>
	<option value="267">PEACH</option>
	<option value="191">PEACH-PALM</option>
	<option value="173">PEA-GENSTOCKS</option>
	<option value="86">PEANUTS</option>
	<option value="88">PEAR</option>
	<option value="116">PEPPERS</option>
	<option value="242">PERILLA</option>
	<option value="277">PERSIMMON</option>
	<option value="83">PHASEOLUS</option>
	<option value="134">PIGEON-PEA</option>
	<option value="222">PILI-NUT</option>
	<option value="133">PINEAPPLE</option>
	<option value="278">PISTACHIO</option>
	<option value="268">PLUM</option>
	<option value="100">PMILLET</option>
	<option value="271">POMEGRANATE</option>
	<option value="291">PORTULACA</option>
	<option value="73">POTATO</option>
	<option value="246">POTENTILLA</option>
	<option value="258">PRUNELLA</option>
	<option value="181">QUINCE</option>
	<option value="209">QUINOA</option>
	<option value="141">RADISH</option>
	<option value="175">RAMBUTAN</option>
	<option value="287">RED-RASPBERRY</option>
	<option value="261">RHUBARB</option>
	<option value="84">RIBES</option>
	<option value="75">RICE</option>
	<option value="290">RICE-GENETICS</option>
	<option value="85">RUBUS</option>
	<option value="76">RYE</option>
	<option value="29">S9-CUCURBITA</option>
	<option value="31">S9-GOURDS</option>
	<option value="152">S9-LEGUME</option>
	<option value="35">S9-LUFFA</option>
	<option value="154">S9-MISC</option>
	<option value="108">SAFFLOWER</option>
	<option value="99">SESAME</option>
	<option value="195">SIMMONDSIA</option>
	<option value="69">SORGHUM</option>
	<option value="289">SORGHUM-GENSTOCKS</option>
	<option value="51">SOYBEAN</option>
	<option value="219">SPINACH</option>
	<option value="91">SQUASH</option>
	<option value="78">STRAWBERRY</option>
	<option value="49">SUGARBEET</option>
	<option value="101">SUGARCANE</option>
	<option value="79">SUNFLOWER</option>
	<option value="147">SWEET-CLOVER</option>
	<option value="109">SWEETPOTATO</option>
	<option value="311">TARO</option>
	<option value="81">TOBACCO</option>
	<option value="305">TOMATILLO</option>
	<option value="50">TOMATO</option>
	<option value="304">TREE-TOMATO</option>
	<option value="128">TREFOIL</option>
	<option value="238">TRIADENUM</option>
	<option value="143">TRIGONELLA</option>
	<option value="72">TRITICALE</option>
	<option value="245">UMBELS-MISC</option>
	<option value="111">VACCINIUM</option>
	<option value="401">VANILLA</option>
	<option value="144">VETCH</option>
	<option value="188">VIGNA</option>
	<option value="199">W6-LEGUMES</option>
	<option value="146">W6-MISC</option>
	<option value="193">WALNUT</option>
	<option value="151">WATERMELON</option>
	<option value="65">WHEAT</option>
	<option value="200">WHEAT-GENETICS</option>
	<option value="198">WHEAT-INTERGENERIC</option>
	<option value="307">WILD-RICE</option>
	<option value="149">WINGBEAN</option>
	<option value="211">WOODY-LANDSCAPE</option>
	<option value="269">WOODY-TRIALS-NC7</option>
	<option value="313">YAM</option>
	<option value="314">YAM-BEAN</option>
	<option value="185">ZINNIA</option>

</select>
                       <input type="submit" name="ctl00$cphBody$btnResetAll" value="New Search" id="ctl00_cphBody_btnResetAll" />
        </div>
    <br />
    
    
        <br />
                


                    </div> <!-- end content -->
                    <div id='footer'>
                    <br />
                    <table width="1024">
                        <tr valign="top">
                        <td width="200"></td>
                        <td align="center"><a href="http://www.croptrust.org"  title="Global Crop Diversity Trust"><img alt="Global Crop Diversity Trust" src="/gringlobal/images/GCDT.png" border="0" /></a></td>
                        <td width="100" align="center"> <a href="http://www.bioversityinternational.org/" title="Bioversity International"><img alt="Bioversity International" src="/gringlobal/images/Bioversity.png"  border="0" /></a></td> 
                        <td align="center" width="100"> <a href="http://www.ars.usda.gov"  title="Agriculture Research Service"><img alt="Agriculture Research Service" src="/gringlobal/images/ARS.png"  border="0" /></a></td>
                        <td align="center" width="100"> <a href="http://www.usda.gov" title="United States Department of Agriculture"><img alt="United States Department of Agriculture" src="/gringlobal/images/USDA.png" border="0" /></a></td>
                        <td width="200" align="center"  valign="middle"><a href="javascript: Disclaimer" onclick="javascript:window.open('/gringlobal/disclaimer.aspx','','scrollbars=yes,titlebar=no,width=600,height=480')"> View disclaimer</a></td>
                        <td width="200"></td>
                        </tr>
                    </table>
                    <br /><br/>
                    </div> 

                </div> <!-- end content-wrapper -->
         </div> <!-- end container -->
     </div><!-- end outer-wrapper -->

    

<script type="text/javascript">
//<![CDATA[
var ctl00_mnuMain_Data = new Object();
ctl00_mnuMain_Data.disappearAfter = 500;
ctl00_mnuMain_Data.horizontalOffset = 0;
ctl00_mnuMain_Data.verticalOffset = 0;
ctl00_mnuMain_Data.iframeUrl = '/gringlobal/WebResource.axd?d=ikjT7WGOFjrgRhZJWh9VvDR6hya5CJZvOjr4tMiD2U6PEZTDr9U-8RETjQjLMK_mgYCY0vKgoj47h-0uK3wkxQPMooU1&t=635588438811475957';
//]]>
</script>
</form>
    <script type='text/javascript'>
        setTimeout(hideFlashes, 30000);

        function hideFlashes() {
            var el = document.getElementById('ctl00_lblMessage');
            if (el) { el.style.display = 'none'; }

            var el = document.getElementById('ctl00_lblMessageRed');
            if (el) { el.style.display = 'none'; }
        }
    </script>
</body>
</html>
