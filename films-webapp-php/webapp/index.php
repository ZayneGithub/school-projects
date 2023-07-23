<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<head>
    <title>Film Information Web Application</title>
    <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8" />

    <link rel="stylesheet" href="./style.css">
    </link>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    </link>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
    </link>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.css">
    </link>

    <script src="https://code.jquery.com/jquery-3.6.3.js" integrity="sha256-nQLuAZGRRcILA+6dMBOvcRh5Pe310sBpanc6+QBmyVM=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js" integrity="sha256-xLD7nhI62fcsEZK2/v8LsBcb4lG7dgULkuXoXB/j91c=" crossorigin="anonymous"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.js" type="text/javascript"></script>
    <script src="./scripts/js/films.js"></script>
    <script src="./scripts/js/utils.js"></script>
    <!--<script src="./scripts/jquery.js" type="text/javascript"></script>
        <script src="./scripts/jquery-ui.js" type="text/javascript"></script>-->
</head>

<body>
    <div class="main-wrapper">
        <div class="ui-widget" align="center">
            <p id="title">Film Information Web Application</p>
            <select id="search-type" name="search-type" title="Search type">
                <option value="TITLE">TITLE</option>
                <option value="ID">ID</option>
            </select>
            <select id="title-condition" name="title-condition" title="Title Condition" style="display: none">
                <option value="EXACT">EXACT</option>
                <option value="LIKE">LIKE</option>
            </select>
            <input id="search-tagged" placeholder="Search (e.g. ROCKY III)">
            <button id="search-button" class="small-button">
                <i class="fa fa-search"></i>
            </button>
            <button id="insert-film-button" class="small-button" title="Insert a film">
                <i class="fa fa-plus"></i>
            </button>
            <button id="get-all-films-button" class="small-button" title="Get all films">
                <i class="fa fa-folder"></i>
            </button>
            <br /> <br />
            <p class="subtitle" style="display: none">Film(s) retrieved from our database:</p>
        </div>
        <div class="ui-widget" align="center">
            <p id="waiting" class="subtitle" style="display: none;">
                <img src="./images/ajax-loader.gif" />
                Waiting for server...
            </p>
        </div>
        <div id="table-wrapper" class="ui-widget" align="center">
            <table id="table" class="display" style="width: 100%;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>TITLE</th>
                        <th>YEAR</th>
                        <th>DIRECTOR</th>
                        <th>STARS</th>
                        <th>REVIEW</th>
                    </tr>
                </thead>
                <tbody></tbody>
                <tfoot>
                    <tr>
                        <th>ID</th>
                        <th>TITLE</th>
                        <th>YEAR</th>
                        <th>DIRECTOR</th>
                        <th>STARS</th>
                        <th>REVIEW</th>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
    <div id="insert-popup-wrapper" class="ui-widget">
        <!---<form id="scripts/insert-form.php" class="form-container" method="post">-->
        <form id="insert-form" class="form-container" method="post">
            <p id="form-title">Insert a Film</p>
            <div class="form-inputs">
                <label>Title <br />
                    <input type="text" placeholder="e.g. The Dark Knight" name="title" maxlength="100" required>
                    <br /> <br /></label>
                <label>Year <br />
                    <input type="number" placeholder="e.g. 2008" name="year" min="1000" max="2022" required>
                    <br /> <br /></label>
                <label>Director <br />
                    <input type="text" placeholder="e.g. Christopher Nolan" name="director" maxlength="100" required>
                    <br /> <br /></label>
                <label>Stars <br />
                    <input type="text" placeholder="e.g. Christian Bale, Heath Ledger" name="stars" maxlength="100" required>
                    <br /> <br /></label>
                <label>Review <br />
                    <input type="text" placeholder="e.g. A dark, gritty, and realistic look..." name="review" required>
                    <br /> <br /></label>
            </div>
            <div class="form-buttons">
                <button type="submit" id="insert-submit">Insert</button>
                <button type="button" id="insert-cancel">Cancel</button>
            </div>
        </form>
    </div>
    <div id="update-popup-wrapper" class="ui-widget">
        <form id="update-form" class="form-container" method="post">
            <p id="form-title">Update Film</p>
            <div class="form-inputs">
                <label>ID<br />
                    <input type="text" name="id" style="color: lightgrey" readonly></label>
                <br /> <br />
                <label>Title <br />
                    <input type="text" placeholder="e.g. The Dark Knight" name="title" maxlength="100" required>
                    <br /> <br /></label>
                <label>Year <br />
                    <input type="number" placeholder="e.g. 2008" name="year" min="1000" max="2022" required>
                    <br /> <br /></label>
                <label>Director <br />
                    <input type="text" placeholder="e.g. Christopher Nolan" name="director" maxlength="100" required>
                    <br /> <br /></label>
                <label>Stars <br />
                    <input type="text" placeholder="e.g. Christian Bale, Heath Ledger" name="stars" maxlength="100" required>
                    <br /> <br /></label>
                <label>Review <br />
                    <input type="text" placeholder="e.g. A dark, gritty, and realistic look..." name="review" required>
                    <br /> <br /></label>
            </div>
            <div class="form-buttons">
                <button type="submit" id="update-submit">Update</button>
                <button type="button" id="update-delete">Delete</button>
                <button type="button" id="update-cancel">Cancel</button>
            </div>
        </form>
    </div>
</body>

</html>