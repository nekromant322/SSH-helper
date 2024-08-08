let copyList  = document.querySelectorAll( '.copy-clipboard' );
let copyArray = Array.prototype.slice.call( copyList );

function tooltipUpdate( button, tooltip, title ) {
    tooltip.dispose();
    button.setAttribute( 'title', title );
    tooltip = new bootstrap.Tooltip( button );
    tooltip.show();

    return tooltip;
}

copyArray.map( function ( copy ) {
    let text    = copy.querySelector( 'span' ).innerText;
    let button  = copy.querySelector( 'i' );
    let tooltip = new bootstrap.Tooltip( button );

    button.addEventListener( 'mouseover', function () {
        tooltip = tooltipUpdate( button, tooltip, 'Скопировать' );
        this.classList.remove( 'bi-clipboard-check' );
        this.classList.add( 'bi-clipboard-plus' );
    } );

    button.addEventListener( 'click', function () {
        window.navigator.clipboard.writeText( "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQCf5l3ZWJCWZsaZIWDm2ajv1oxlKevRDE3f0M1PLmPcy/gCnMLxQgX1mbMTh8XOt9M1ql9YrwVJU+EPxfOcUNkceGeyEVV7NWN6e85q4UwVCDE9ThPWTOqz8tkWYIqVqWwzp/cA+9qzwrGNq7UZrRiJ85uK1+wdKwwJmycXpJMdDvg1VDwIWNxXoYKiPTm6R/2+hVrvkDJD70GgAyDbKQX2rqKLs6aSlWVChfIYJB+mEgC5MytYWlEiCiEavyYWWryPiicfzZCanziGdbS1mJZU8Aq3c5Z8JR12dlZkB6DBT8nbWjkPWdE7gvznJQjYct4KTrPYcrUbPN0uSfkQGDGtUz8FHlZobHruJy4SQp8CvgRrq+yyZPycSui8uVMZCpeAgG7H/Iw+MmeIZaKpGV+X7H0SUY3zXtXGudNyj6Vj15o6fpZGtAdovaHFP3dpYNfPfhbrvww21NP9DoRtWkG6BQpFTW8f1rYSSHuuBf6WjTYoq+BiHt8hIThqUCAurXLzJpoY2OWlT5dF/+TiDTEY8we1VMn5nJ5gVlwa25uLGX1t8Sj9W50aMWrhvP3YPXJ4sOO0lfw2Va8Chs/o0NBOgptnsXhspHJSBfCpQ+yHICQfy7yIivVFnd0crEtDcm6TI7yMaYeaagYXRPLu1R58wWfDWEadjTx1nAZaIpfIeQ== yoasakura13@gmail.com" );
        tooltip = tooltipUpdate( button, tooltip, 'Готово!' );
        this.classList.remove( 'bi-clipboard-plus' );
        this.classList.add( 'bi-clipboard-check' );
    } );
} );